package thi.cnd.careerservice.event.internal;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.support.RetryTemplate;

import com.eventstore.dbclient.CreatePersistentSubscriptionToAllOptions;
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;
import com.eventstore.dbclient.NackAction;
import com.eventstore.dbclient.NamedConsumerStrategy;
import com.eventstore.dbclient.PersistentSubscription;
import com.eventstore.dbclient.PersistentSubscriptionListener;
import com.eventstore.dbclient.PersistentSubscriptionToAllInfo;
import com.eventstore.dbclient.ResolvedEvent;
import com.eventstore.dbclient.SubscriptionFilter;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import thi.cnd.careerservice.event.config.EventStoreSerializer;
import thi.cnd.careerservice.event.internal.exception.EventStoreConnectionException;
import thi.cnd.careerservice.shared.event.InternalDomainEventListener;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;

/**
 * Listens to the event store db for events and passes them to the provided processor.
 * This is the central entry point for domain internal event listeners.
 */
public class EventStoreListener extends PersistentSubscriptionListener {

    private final ApplicationContext appContext;
    private final EventStoreSerializer eventStoreSerializer;
    private final EventStoreDBPersistentSubscriptionsClient eventStoreDBClient;
    private final String subscriptionGroupName;
    private final CreatePersistentSubscriptionToAllOptions options;
    private final Consumer<RecordedDomainEventWithMetadata<? extends DomainEvent>> consumer;

    private PersistentSubscription subscription;
    private boolean isRunning;

    private final RetryTemplate retryTemplate = RetryTemplate.builder()
        .retryOn(StatusRuntimeException.class)
        .exponentialBackoff(100, 2, 5000)
        .withTimeout(Duration.ofSeconds(30))
        .build();

    private final Logger logger = LoggerFactory.getLogger(EventStoreListener.class);

    public EventStoreListener(ApplicationContext appContext, EventStoreSerializer eventStoreSerializer,
        EventStoreDBPersistentSubscriptionsClient eventStoreDBClient, InternalDomainEventListener listener) {
        this.appContext = appContext;
        this.eventStoreSerializer = eventStoreSerializer;
        this.eventStoreDBClient = eventStoreDBClient;
        this.subscriptionGroupName = listener.getUniqueListenerName();
        this.options = defaultOptions(eventStoreSerializer.getEventTypePrefix());
        this.consumer = listener::acceptEvent;
    }

    /**
     * Triggered every time a new event is received.
     */
    @Override
    public void onEvent(PersistentSubscription subscription, int retryCount, ResolvedEvent resolvedEvent) {
        try {
            eventStoreSerializer
                .deserializeEvents(resolvedEvent)
                .map(event -> RecordedDomainEventWithMetadata.create(resolvedEvent, event))
                .ifPresent(consumer);
        } catch (Throwable e) {
            logger.error("Could not process event", e);
            subscription.nack(NackAction.Retry, e.getMessage(), resolvedEvent);
            return;
        }

        subscription.ack(resolvedEvent);
    }

    public void startListeningToEventStore() {
        eventStoreDBClient.listToAll().handleAsync((infoGroup, error) -> {
            if (error != null) {
                return handleInitialConnectionError(error);
            }

            var subscriptionGroup = findExistingSubscriptionGroup(infoGroup, subscriptionGroupName);
            return subscriptionGroup
                .map(ignored -> subscribeToPersistentSubscription())
                .orElseGet(this::createAndListenToNewSubscriptionGroup);
        }).whenComplete((activeSubscription, error) -> {
            this.subscription = activeSubscription;

            if (error != null) {
                logger.error("Error occurred while trying to listen to event store $all stream", error);
            }

            if (this.subscription == null) {
                reconnectToEventStore();
            }
        });
    }

    private Optional<PersistentSubscriptionToAllInfo> findExistingSubscriptionGroup(List<PersistentSubscriptionToAllInfo> infoGroup,
        String name) {
        return infoGroup.stream()
            .filter(info -> info.getGroupName().equals(name))
            .findFirst();
    }


    private PersistentSubscription handleInitialConnectionError(Throwable error) {
        if (error instanceof StatusRuntimeException statusRuntimeException &&
            (statusRuntimeException.getStatus().getCode().equals(Status.NOT_FOUND.getCode()))) {
            // Happens if there are no subscription groups yet
            return createAndListenToNewSubscriptionGroup();
        }

        throw new EventStoreConnectionException(error);
    }

    /**
     * Create new subscription group if it does not exist yet and subscribe to it
     */
    private PersistentSubscription createAndListenToNewSubscriptionGroup() {
        try {
            eventStoreDBClient.createToAll(subscriptionGroupName, options).get();
            return subscribeToPersistentSubscription();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EventStoreConnectionException(e);
        } catch (ExecutionException e) {
            throw new EventStoreConnectionException(e);
        }
    }

    private PersistentSubscription subscribeToPersistentSubscription() {
        try {
            return eventStoreDBClient.subscribeToAll(subscriptionGroupName, this).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new EventStoreConnectionException(e);
        } catch (ExecutionException e) {
            throw new EventStoreConnectionException(e);
        }
    }

    /**
     * Default persistent subscription options to use if the subscription does not exist
     */
    private CreatePersistentSubscriptionToAllOptions defaultOptions(String eventTypePrefix) {
        return CreatePersistentSubscriptionToAllOptions.get()
            .filter(SubscriptionFilter.newBuilder()
                .addEventTypePrefix(eventTypePrefix)
                .build())
            .fromStart()
            .maxRetryCount(5)
            .readBatchSize(10)
            .checkpointLowerBound(0)
            .checkpointUpperBound(1)
            .namedConsumerStrategy(NamedConsumerStrategy.ROUND_ROBIN);
    }

    /**
     * Reconnect to event store in case connection was lost or could not be established
     */
    private void reconnectToEventStore() {
        this.isRunning = false;
        this.subscription = null;

        try {
            retryTemplate.execute(callback -> {
                logger.error("Trying to reconnect to event store. Retry {}", callback.getRetryCount());
                this.subscription = subscribeToPersistentSubscription();
                this.isRunning = true;
                return null;
            });
        } catch (Throwable e) {
            logger.error("Exiting application because the subscription to {} failed with message {}", subscriptionGroupName, e.getMessage());
            SpringApplication.exit(appContext, () -> -2);
        }
    }

    @Override
    public void onError(PersistentSubscription subscription, Throwable throwable) {
        logger.error("Connection to event store was dropped. Trying to reconnect", throwable);
        reconnectToEventStore();
    }

    @Override
    public void onCancelled(PersistentSubscription subscription) {
        logger.error("Connection to event store was cancelled. Trying to reconnect...");
        reconnectToEventStore();
    }

    @Override
    public void onConfirmation(PersistentSubscription subscription) {
        super.onConfirmation(subscription);
        this.isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        if (isRunning()) {
            subscription.stop();
            isRunning = false;
        }
    }

}


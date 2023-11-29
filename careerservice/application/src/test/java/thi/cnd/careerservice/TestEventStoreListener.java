package thi.cnd.careerservice;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;
import thi.cnd.careerservice.event.internal.EventStoreListener;
import thi.cnd.careerservice.shared.event.InternalDomainEventListener;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;

@Component
@Profile("integrationtest")
public class TestEventStoreListener implements InternalDomainEventListener, SmartLifecycle {

    private final EventStoreListener listener;

    private final Queue<RecordedDomainEventWithMetadata<? extends DomainEvent>> receivedEvents = new LinkedList<>();
    private final RetryTemplate retryTemplate;

    public TestEventStoreListener(
        ApplicationContext appContext,
        EventStoreSerializer serializer,
        EventStoreDBPersistentSubscriptionsClient client,
        RetryTemplate retryTemplate) {
        this.listener = new EventStoreListener(appContext, serializer, client, this);
        this.retryTemplate = retryTemplate;
    }

    @Override
    public <E extends DomainEvent> void acceptEvent(RecordedDomainEventWithMetadata<E> event) {
        receivedEvents.add(event);
    }

    public void clearEventQueue() {
        receivedEvents.clear();
    }

    public boolean isEmpty() {
        return receivedEvents.isEmpty();
    }

    public <T extends DomainEvent> T awaitNextSpecificEvent(Class<T> clazz) {
        return awaitNextSpecificEvent(clazz, 0);
    }

    public <T extends DomainEvent> T awaitNextSpecificEvent(Class<T> clazz, final int skipEvents) {
        return poll(context -> {
            if (skipEvents < receivedEvents.size()) {
                for (int i = skipEvents; i > 0; i--) {
                    receivedEvents.remove();
                }

                if (receivedEvents.isEmpty() || !(receivedEvents.peek().data().getClass().equals(clazz))) {
                    throw new IllegalStateException("No event received. Current Retry: " + context.getRetryCount());
                }

                return (T) receivedEvents.remove().data();
            }

            throw new IllegalStateException("Not enough events received. Current Retry: " + context.getRetryCount());
        });
    }

    public DomainEvent peakFirst(Predicate<DomainEvent> filter) {
        return poll(context ->
            receivedEvents.stream().filter(item -> filter.test(item.data())).findFirst()
                .orElseThrow(() -> new IllegalStateException("Timeout while waiting until predicate succeeds."))
                .data()
        );
    }

    public <T extends DomainEvent> T deleteFirst(Class<T> clazz) {
        return poll(context ->
            (T) deleteFirst(item -> item.getClass().equals(clazz))
        );
    }

    public DomainEvent deleteFirst(Predicate<DomainEvent> filter) {
        return poll(context ->
            {
                var event = receivedEvents.stream().filter(item -> filter.test(item.data())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("Timeout while waiting until predicate succeeds."));

                receivedEvents.remove(event);

                return event.data();
            }
        );
    }


    public boolean waitUntil(Predicate<List<? extends DomainEvent>> filter) {
        return poll(context -> {
                if (filter.test(receivedEvents.stream().map(RecordedDomainEventWithMetadata::data).toList())) {
                    return true;
                }

                throw new IllegalStateException("Timeout while waiting until predicate succeeds.");
            }
        );
    }

    public boolean deleteTillMatch(Predicate<DomainEvent> filter) {
        return poll(context -> {
            while (!receivedEvents.isEmpty()) {
                if (filter.test(receivedEvents.peek().data())) {
                    return true;
                } else {
                    receivedEvents.remove();
                }
            }
            throw new IllegalStateException("Did not find event matching provided filter till timeout");
        });
    }

    public DomainEvent awaitNextEvent() {
        return poll(context -> {
            if (receivedEvents.isEmpty()) {
                throw new IllegalStateException("No event received. Current Retry: " + context.getRetryCount());
            }
            return receivedEvents.remove().data();
        });
    }

    private <T, E extends Throwable> T poll(RetryCallback<T, E> retryCallback) throws E {
        return retryTemplate.execute(retryCallback);
    }

    @Override
    public String getUniqueListenerName() {
        return "TestListener";
    }

    @Override
    public void stop(Runnable callback) {
        listener.stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void start() {
        listener.startListeningToEventStore();
    }

    @Override
    public void stop() {
        this.stop(() -> {
        });
    }

    @Override
    public boolean isRunning() {
        return listener.isRunning();
    }
}

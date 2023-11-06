package thi.cnd.careerservice.event.internal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.shared.view.ViewProjector;
import thi.cnd.careerservice.event.config.EventStoreSerializer;

/**
 * Projects internal domain events to the query side of the CQRS application in order to represent the current state of the domain objects.
 */
public abstract class GenericViewProjector implements SmartLifecycle, ViewProjector {

    private final EventStoreListener eventStoreListener;

    protected GenericViewProjector(
        ApplicationContext appContext, EventStoreSerializer serializer, EventStoreDBPersistentSubscriptionsClient client
    ) {
        this.eventStoreListener = new EventStoreListener(appContext, serializer, client, this);
    }

    @Override
    public void stop(Runnable callback) {
        eventStoreListener.stop();
        callback.run();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void start() {
        eventStoreListener.startListeningToEventStore();
    }

    @Override
    public void stop() {
        this.stop(() -> {});
    }

    @Override
    public boolean isRunning() {
        return eventStoreListener.isRunning();
    }
}

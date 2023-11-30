package thi.cnd.careerservice.event.internal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;
import thi.cnd.careerservice.shared.event.EventListener;

/**
 * Listens to internal domain events and fires according to them new internal domain events, which results in updates of existing data.
 */
public abstract class GenericInternalEventListener implements SmartLifecycle, EventListener {

    protected final EventStoreListener eventStoreListener;

    protected GenericInternalEventListener(ApplicationContext appContext, EventStoreSerializer serializer,
        EventStoreDBPersistentSubscriptionsClient client) {
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

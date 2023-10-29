package thi.cnd.careerservice.shared.event;

import java.util.List;
import java.util.function.Consumer;

import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.AggregateRoot;

/**
 * Event store for publishing and retrieving domain events for an {@link AggregateRoot}.
 *
 * @param <ID> ID type of the AggregateRoot
 * @param <S>  The {@link AggregateRoot} itself
 * @param <E>  The {@link DomainEvent}s that are fired from this AggregateRoot
 */
public interface EventStore<ID, S extends AggregateRoot<ID, S, E>, E extends DomainEvent> {

    /**
     * Retrieves an instance of the aggregate root, updates it with the provided consumer, and stores all fired events in the event store.
     * If the provided {@link ETag} does not match, then an exception is thrown instead.
     */
    DataWithETag<S> updateAndPublish(ID id, ETag expectedRevision, Consumer<S> update);

    /**
     * Retrieves all events that were fired for the aggregate root identified by the provided id.
     */
    List<E> readAllEvents(ID id);

    DataWithETag<S> getWithVersion(ID id);

    S get(ID id);

    /**
     * This method creates a new instance of an aggregate root by creating a new event stream with the provided id.
     */
    DataWithETag<S> publishFirstEventAndCreateNewEventStream(S entity);

    /**
     * Fires the provided events to the event stream <b>without validating the aggregate roots state</b>.
     * This method should only be used if the current state is not required to evaluate if the event can be fired.
     */
    DataWithETag<S> forcePublishEvents(ID id, List<E> events);

    DataWithETag<S> forcePublishEvents(ID id, E ...events);
}

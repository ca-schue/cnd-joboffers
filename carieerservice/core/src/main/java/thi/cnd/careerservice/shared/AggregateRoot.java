package thi.cnd.careerservice.shared;

import java.util.LinkedList;
import java.util.Queue;

import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.exception.ResourceIsDeletedException;
import lombok.Getter;

/**
 * @param <ID> The type of the id to identify this aggregate root
 * @param <S> The concrete type of the aggregate root, so the class extending this abstract class
 * @param <E> The event type fired by this aggregate root
 */
@Getter
public abstract class AggregateRoot<ID, S extends AggregateRoot, E extends DomainEvent> {

    /**
     * The current version of the aggregate root. Every change increments this version by one.
     */
    private int version;

    /**
     * Events that were fired but not yet committed/published into the event store.
     */
    private final Queue<E> uncommittedEvents = new LinkedList<>();

    /**
     * @return the id of the aggregate root object.
     */
    public abstract ID getId();

    /**
     * @return true if this object should be seen as deleted and therefore not be available ofr any querying or changes.
     */
    public abstract boolean isDeleted();

    /**
     * Apply the event to this aggregate root by changing its state
     */
    public abstract void apply(E event);

    /**
     * Fires the event essentially adding it to the uncommitted events, applying the event and incrementing the aggregate root version.
     */
    protected S fire(E event) {
        if (isDeleted()) {
            throw new ResourceIsDeletedException();
        }

        uncommittedEvents.add(event);
        apply(event);
        version++;
        return (S) this;
    }



}

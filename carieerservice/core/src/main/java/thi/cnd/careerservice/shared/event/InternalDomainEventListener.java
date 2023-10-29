package thi.cnd.careerservice.shared.event;

import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;

/**
 * Listens to an {@link EventStore} for internal domain events
 */
public interface InternalDomainEventListener {

    <E extends DomainEvent> void acceptEvent(RecordedDomainEventWithMetadata<E> event);

    String getUniqueListenerName();

}

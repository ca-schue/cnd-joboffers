package thi.cnd.careerservice.event;

import thi.cnd.careerservice.shared.event.RegisteredDomainEvent;
import thi.cnd.careerservice.shared.event.model.DomainEvent;

/**
 * Maps all {@link DomainEvent}s to their name provided by the {@link RegisteredDomainEvent} annotation.
 * This class is required to map instances of events to a string representation of their type and reverting the type name back to their
 * corresponding type:
 * <ul>
 *     <li>ExampleEvent.class -> {@link EventTypeResolver} ->  "DomainEvent_ExampleEvent" -> Store in EventStore</li>
 *     <li>Retrieve from EventStore -> "DomainEvent_ExampleEvent" -> {@link EventTypeResolver} -> ExampleEvent.class</li>
 * </ul>
 */
public interface EventTypeResolver {

    String getEventName(Class<? extends DomainEvent> clazz);

    <E extends DomainEvent> Class<E> getEventClassOrThrow(String eventName);

}

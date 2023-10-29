package thi.cnd.careerservice.event;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import thi.cnd.careerservice.shared.event.RegisteredDomainEvent;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;


/**
 * {@link EventTypeResolver} that stores the mapping between {@link DomainEvent}s and event names in memory.
 */
@Component
public class InMemoryEventTypeResolver implements EventTypeResolver {

    private final String globalEventTypePrefix;
    private final BiMap<String, Class<? extends DomainEvent>> eventTypes = HashBiMap.create();

    public InMemoryEventTypeResolver(@Value("${event.eventTypePrefix}") String globalEventTypePrefix) {
        this.globalEventTypePrefix = globalEventTypePrefix;
        storeAllDomainEventClasses();
    }

    private void storeAllDomainEventClasses() {
        Deque<Class<DomainEvent>> stack = new ArrayDeque<>();

        // Initially fill the stack with all domain events implementing the DomainEvent class
        Arrays.stream(DomainEvent.class.getPermittedSubclasses()).forEach(subclass -> stack.push((Class<DomainEvent>) subclass));

        while (!stack.isEmpty()) {
            Class<DomainEvent> node = stack.pop();
            var subtypes = node.getPermittedSubclasses();

            if (subtypes == null) {
                addRegisteredEventToInMemoryMap(node, true);
            } else {
                addRegisteredEventToInMemoryMap(node, false);

                // Has subtypes, continue downwards until we don't have any subtypes anymore.
                for (int i = subtypes.length - 1; i >= 0; i--) {
                    stack.push((Class<DomainEvent>) subtypes[i]);
                }
            }
        }
    }

    public void addRegisteredEventToInMemoryMap(Class<DomainEvent> clazz, boolean throwIfAnnotationIsMissing) {
        var annotation = clazz.getAnnotation(RegisteredDomainEvent.class);

        if (annotation == null && throwIfAnnotationIsMissing) {
            throw new IdentifiedRuntimeException(() -> "Event class found without RegisteredEvent annotation");
        } else if (annotation != null) {
            var eventName = annotation.name();
            eventTypes.put(globalEventTypePrefix + "_" + eventName, clazz);
        }
    }

    /**
     * Get event name for the provided {@link DomainEvent} class.
     */
    @Override
    public String getEventName(Class<? extends DomainEvent> clazz) {
        return eventTypes.inverse().get(clazz);
    }

    /**
     * Get {@link DomainEvent} class for the provided event name.
     */
    @Override
    public <E extends DomainEvent> Class<E> getEventClassOrThrow(String eventName) {
        return this.<E>getEventClass(eventName).orElseThrow(() ->
            new IdentifiedRuntimeException(() -> String.format("Event class for event name %s not found!", eventName))
        );
    }

    private <E extends DomainEvent> Optional<Class<E>> getEventClass(String eventName) {
        var eventType = eventTypes.get(eventName);

        if (eventType == null) {
            return Optional.empty();
        }

        try {
            return Optional.of((Class<E>) eventType);
        } catch (ClassCastException e) {
            throw new IdentifiedRuntimeException(() -> "Event class could not be casted to expected type!", e);
        }
    }

}

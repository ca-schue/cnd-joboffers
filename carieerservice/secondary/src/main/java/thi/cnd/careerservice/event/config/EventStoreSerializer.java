package thi.cnd.careerservice.event.config;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventDataBuilder;
import com.eventstore.dbclient.ResolvedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import thi.cnd.careerservice.event.EventTypeResolver;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import jakarta.annotation.Nonnull;
import lombok.Getter;

/**
 * Serializes {@link DomainEvent}s to {@link EventData} and deserializes {@link ResolvedEvent} to {@link DomainEvent}s
 */
@Component
@Validated
public class EventStoreSerializer {

    private final ObjectMapper objectMapper;
    private final EventTypeResolver eventTypeResolver;

    @Getter
    private final String eventTypePrefix;

    private final Logger logger = LoggerFactory.getLogger(EventStoreSerializer.class);

    public EventStoreSerializer(
        @Qualifier("EventStoreObjectMapper") ObjectMapper objectMapper,
        EventTypeResolver eventTypeResolver,
        @Value("${event.eventTypePrefix}") String eventTypePrefix) {
        this.objectMapper = objectMapper;
        this.eventTypeResolver = eventTypeResolver;
        this.eventTypePrefix = eventTypePrefix;
    }

    /**
     * Serializes a {@link DomainEvent} to be stored in the event store
     */
    @Nonnull
    public EventData serialize(@Nonnull DomainEvent event) {
        try {
            return EventDataBuilder.json(
                UUID.randomUUID(),
                eventTypeResolver.getEventName(event.getClass()),
                objectMapper.writeValueAsBytes(event)
            ).build();
        } catch (JsonProcessingException e) {
            throw new IdentifiedRuntimeException(() -> "Could not serialize event into json to insert into event store.", e);
        }
    }

    /**
     * Deserializes already persisted {@link ResolvedEvent} into the corresponding {@link DomainEvent}
     */
    @Nonnull
    public <E extends DomainEvent> Optional<E> deserializeEvents(@Nonnull ResolvedEvent resolvedEvent) {
        var resolvedEventName = resolvedEvent.getEvent().getEventType();
        var resolvedEventClass = eventTypeResolver.<E>getEventClassOrThrow(resolvedEventName);

        try {
            var event = objectMapper.readValue(resolvedEvent.getEvent().getEventData(), resolvedEventClass);

            if (event == null) {
                logger.warn("Found empty event within event type {}", resolvedEventName);
            }

            return Optional.ofNullable(event);
        } catch (IOException e) {
            throw new IdentifiedRuntimeException(() -> "Could not deserialize event data into event object.", e);
        }
    }

}

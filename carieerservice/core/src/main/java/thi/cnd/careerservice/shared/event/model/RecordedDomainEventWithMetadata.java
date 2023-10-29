package thi.cnd.careerservice.shared.event.model;

import com.eventstore.dbclient.ResolvedEvent;

/**
 * Already persisted event with its corresponding metadata
 */
public record RecordedDomainEventWithMetadata<E extends DomainEvent>(
    E data,
    RecordedEventMetadata metaData
) {

    public static <E extends DomainEvent> RecordedDomainEventWithMetadata<E> create(ResolvedEvent metaData, E event) {
        return new RecordedDomainEventWithMetadata<>(
            event,
            new RecordedEventMetadata(
                metaData.getEvent().getEventId().toString(),
                metaData.getEvent().getRevision(),
                metaData.getEvent().getPosition().getCommitUnsigned(),
                metaData.getEvent().getEventType()
            )
        );
    }

    public <S extends DomainEvent> RecordedDomainEventWithMetadata<S> with(S data) {
        return new RecordedDomainEventWithMetadata<>(
            data,
            new RecordedEventMetadata(
                this.metaData.id(),
                this.metaData.streamPosition(),
                this.metaData.logPosition(),
                this.metaData.type()
            )
        );
    }

}

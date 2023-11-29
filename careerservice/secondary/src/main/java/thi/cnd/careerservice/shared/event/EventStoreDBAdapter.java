package thi.cnd.careerservice.shared.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ExpectedRevision;
import com.eventstore.dbclient.ReadResult;
import com.eventstore.dbclient.ReadStreamOptions;
import com.eventstore.dbclient.StreamNotFoundException;
import com.eventstore.dbclient.WrongExpectedVersionException;

import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;
import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.shared.event.model.DeletionEvent;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.exception.ExpectedRevisionNotMatchingException;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ResourceNotFoundException;
import thi.cnd.careerservice.shared.AggregateRoot;
import lombok.AllArgsConstructor;

/**
 * Implementing an {@link EventStore} for an {@link AggregateRoot} to be stored and retrieved in an EventStoreDB
 */
@AllArgsConstructor
public class EventStoreDBAdapter<ID, S extends AggregateRoot<ID, S, E>, E extends DomainEvent> implements EventStore<ID, S, E> {

    private final EventStoreDBClient eventStoreClient;
    private final EventStoreSerializer eventStoreSerializer;

    private final Function<ID, String> mapToStreamId;
    private final Supplier<S> buildEmpty;

    @Override
    public DataWithETag<S> updateAndPublish(ID id, ETag expectedRevision, Consumer<S> update) {
        var events = readAllEventsForStreamId(mapToStreamId.apply(id), Optional.of(expectedRevision)).getData();
        var currentState = rebuildDomainObjectFromEvents(events);
        update.accept(currentState);
        return publishEvents(currentState, expectedRevision);
    }

    @Override
    public List<E> readAllEvents(ID id) {
        return readAllEventsForStreamId(mapToStreamId.apply(id), Optional.empty()).getData();
    }

    @Override
    public DataWithETag<S> getWithVersion(ID id) {
        var dataWithETag = readAllEventsForStreamId(mapToStreamId.apply(id), Optional.empty());
        return new DataWithETag<>(dataWithETag.getETag(), rebuildDomainObjectFromEvents(dataWithETag.getData()));
    }

    @Override
    public S get(ID id) {
        return getWithVersion(id).getData();
    }

    private DataWithETag<List<E>> readAllEventsForStreamId(String streamId, Optional<ETag> eTag) {
        try {
            var result = eventStoreClient.readStream(streamId, ReadStreamOptions.get()).get();

            eTag.ifPresent(expectedRevision -> validateExpectedRevisionWithActualRevision(result, expectedRevision.getValue()));

            var events =  result.getEvents().stream()
                .map(eventStoreSerializer::<E>deserializeEvents)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

            return new DataWithETag<>(ETag.fromValue(result.getLastStreamPosition()), events);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IdentifiedRuntimeException(() -> "Could not retrieve events for stream " + streamId, e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof StreamNotFoundException) {
                throw new ResourceNotFoundException("Could not find resource with id " + streamId);
            }
            throw new IdentifiedRuntimeException(() -> "Could not retrieve events for stream " + streamId, e);
        }
    }

    private static void validateExpectedRevisionWithActualRevision(ReadResult result, long expectedRevision) {
        var currentRevision = result.getLastStreamPosition();
        if (currentRevision != expectedRevision) {
            throw new ExpectedRevisionNotMatchingException(currentRevision, expectedRevision);
        }
    }

    private S rebuildDomainObjectFromEvents(List<E> events) {
        var initialState = buildEmpty.get();
        events.forEach(initialState::apply);
        return initialState;
    }

    @Override
    public DataWithETag<S> publishFirstEventAndCreateNewEventStream(S entity) {
        return publishEvents(entity, AppendToStreamOptions.get().expectedRevision(ExpectedRevision.noStream()));
    }

    @Override
    public DataWithETag<S> forcePublishEvents(ID id, List<E> events) {
        return publishEvents(id, null, new LinkedList<>(events), AppendToStreamOptions.get());
    }

    @Override
    public DataWithETag<S> forcePublishEvents(ID id, E ...events) {
        return publishEvents(id, null, new LinkedList<>(List.of(events)), AppendToStreamOptions.get());
    }

    private DataWithETag<S> publishEvents(S entity, ETag expectedRevision) {
        return publishEvents(entity, AppendToStreamOptions.get().expectedRevision(expectedRevision.getValue()));
    }

    private DataWithETag<S> publishEvents(S entity, AppendToStreamOptions options) {
        return publishEvents(entity.getId(), entity, entity.getUncommittedEvents(), options);
    }

    private DataWithETag<S> publishEvents(ID id, S entity, Queue<E> events, AppendToStreamOptions options) {
        List<EventData> toBeSavedEvents = new ArrayList<>();
        boolean containsDeletionEvent = false;

        for (DomainEvent event : events) {
            toBeSavedEvents.add(eventStoreSerializer.serialize(event));
            if (event instanceof DeletionEvent) {
                containsDeletionEvent = true;
                break;
            }
        }

        try {
            var eventStreamId = mapToStreamId.apply(id);
            var response = eventStoreClient.appendToStream(eventStreamId, options, toBeSavedEvents.iterator()).get();

            if (containsDeletionEvent) {
                eventStoreClient.deleteStream(eventStreamId);
            }

            var nextExceptedRevision = response.getNextExpectedRevision().toRawLong();
            return new DataWithETag<>(ETag.weak(nextExceptedRevision), entity);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IdentifiedRuntimeException(() -> "Error while trying to save events to the event store", e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof WrongExpectedVersionException cause) {
                throw new ExpectedRevisionNotMatchingException(
                    cause.getActualVersion().toRawLong(),
                    cause.getNextExpectedRevision().toRawLong()
                );
            }

            throw new IdentifiedRuntimeException(() -> "Error while trying to save events to the event store", e);
        }
    }

}

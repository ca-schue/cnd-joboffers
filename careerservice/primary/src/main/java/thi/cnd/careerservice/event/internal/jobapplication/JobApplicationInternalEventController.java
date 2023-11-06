package thi.cnd.careerservice.event.internal.jobapplication;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.event.internal.GenericInternalEventController;
import thi.cnd.careerservice.shared.event.EventControllerName;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferAttributesChanged;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferCreated;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferDeleted;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferStatusUpdated;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferTitleChanged;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.jobapplication.domain.JobApplicationCrossAggregateEventHandler;
import thi.cnd.careerservice.event.config.EventStoreSerializer;

@Component
public class JobApplicationInternalEventController extends GenericInternalEventController {

    private final JobApplicationCrossAggregateEventHandler eventHandler;

    public JobApplicationInternalEventController(
        ApplicationContext appContext,
        EventStoreSerializer eventStoreSerializer,
        EventStoreDBPersistentSubscriptionsClient eventStoreDBClient,
        JobApplicationCrossAggregateEventHandler eventHandler) {
        super(appContext, eventStoreSerializer, eventStoreDBClient);
        this.eventHandler = eventHandler;
    }

    @Override
    public <E extends DomainEvent> void acceptEvent(RecordedDomainEventWithMetadata<E> eventWithMetadata) {
        if (!(eventWithMetadata.data() instanceof JobOfferEvent jobOfferEvent)) {
            return;
        }

        switch (jobOfferEvent) {
            case JobOfferStatusUpdated event -> eventHandler.applyJobOfferStatusChanged(event.id(), event.status());
            case JobOfferTitleChanged event -> eventHandler.updateJobOfferTitle(event.id(), event.title());
            case JobOfferDeleted event -> eventHandler.deleteJobApplication(event.id());
            case JobOfferAttributesChanged event -> {}
            case JobOfferCreated event -> {}
        };
    }

    @Override
    public EventControllerName getControllerName() {
        return EventControllerName.JOB_APPLICATION;
    }

}

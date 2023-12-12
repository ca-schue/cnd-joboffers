package thi.cnd.careerservice.event.internal.joboffer.query;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferAttributesChanged;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferCreated;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferDeleted;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferStatusUpdated;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent.JobOfferTitleChanged;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.joboffer.query.application.service.UpdateJobOfferViewService;
import thi.cnd.careerservice.shared.view.ViewProjectorName;
import thi.cnd.careerservice.event.internal.GenericViewProjector;
import thi.cnd.careerservice.shared.event.config.EventStoreSerializer;

@Component
public class JobOfferViewProjector extends GenericViewProjector {

    private final UpdateJobOfferViewService updateViewService;

    public JobOfferViewProjector(
        ApplicationContext appContext,
        EventStoreSerializer eventStoreSerializer,
        EventStoreDBPersistentSubscriptionsClient eventStoreDBClient,
        UpdateJobOfferViewService updateViewService) {
        super(appContext, eventStoreSerializer, eventStoreDBClient);
        this.updateViewService = updateViewService;
    }

    @Override
    public ViewProjectorName getUniqueViewName() {
        return ViewProjectorName.JOB_OFFER;
    }

    @Override
    public <E extends DomainEvent> void acceptEvent(RecordedDomainEventWithMetadata<E> genericEvent) {
        if (!(genericEvent.data() instanceof JobOfferEvent jobOfferEvent)) {
            return;
        }

        switch (jobOfferEvent) {
            case JobOfferCreated event -> updateViewService.newJobOfferCreated(genericEvent.with(event));
            case JobOfferStatusUpdated event -> updateViewService.updateJobOfferStatus(genericEvent.with(event));
            case JobOfferTitleChanged event -> updateViewService.updateJobOfferTitle(genericEvent.with(event));
            case JobOfferAttributesChanged event -> updateViewService.updateJobOfferAttributes(genericEvent.with(event));
            case JobOfferDeleted event -> updateViewService.deleteJobOffer(genericEvent.with(event));
        };
    }

}

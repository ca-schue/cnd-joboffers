package thi.cnd.careerservice.event.internal.jobapplication;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;

import thi.cnd.careerservice.event.config.EventStoreSerializer;
import thi.cnd.careerservice.event.internal.GenericViewProjector;
import thi.cnd.careerservice.jobapplication.view.update.UpdateJobApplicationViewService;
import thi.cnd.careerservice.shared.event.model.DomainEvent;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationContentChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationCreated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationDeleted;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationStatusChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationCompanyNameUpdated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.shared.view.ViewProjectorName;

@Component
public class JobApplicationViewProjector extends GenericViewProjector {

    private final UpdateJobApplicationViewService updateViewService;

    public JobApplicationViewProjector(
        ApplicationContext appContext,
        EventStoreSerializer eventStoreSerializer,
        EventStoreDBPersistentSubscriptionsClient eventStoreDBClient,
        UpdateJobApplicationViewService updateViewService
    ) {
        super(appContext, eventStoreSerializer, eventStoreDBClient);
        this.updateViewService = updateViewService;
    }

    @Override
    public ViewProjectorName getUniqueViewName() {
        return ViewProjectorName.JOB_APPLICATION;
    }

    @Override
    public <E extends DomainEvent> void acceptEvent(RecordedDomainEventWithMetadata<E> genericEvent) {
        if (!(genericEvent.data() instanceof JobApplicationEvent jobApplicationEvent)) {
            return;
        }

        switch (jobApplicationEvent) {
            case JobApplicationCreated event -> updateViewService.newJobApplicationCreated(genericEvent.with(event));
            case JobApplicationDeleted event -> updateViewService.deleteJobApplication(genericEvent.with(event));
            case JobApplicationStatusChanged event -> updateViewService.updateStatus(genericEvent.with(event));
            case JobApplicationContentChanged event -> updateViewService.updateContent(genericEvent.with(event));
            case JobApplicationJobOfferUpdated event -> updateViewService.updateJobOfferData(genericEvent.with(event));
            case JobApplicationCompanyNameUpdated event -> updateViewService.updateCompanyName(genericEvent.with(event));
        };
    }

}

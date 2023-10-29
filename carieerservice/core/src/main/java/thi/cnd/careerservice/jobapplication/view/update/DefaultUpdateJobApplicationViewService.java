package thi.cnd.careerservice.jobapplication.view.update;

import org.springframework.stereotype.Component;

import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationContentChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationCreated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationDeleted;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationStatusChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationCompanyNameUpdated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import lombok.RequiredArgsConstructor;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

@Component
@RequiredArgsConstructor
public class DefaultUpdateJobApplicationViewService implements UpdateJobApplicationViewService {

    private final JobApplicationUpdateViewPort jobApplicationUpdateViewPort;

    @Override
    public void newJobApplicationCreated(RecordedDomainEventWithMetadata<JobApplicationCreated> eventWithMetadata) {
        jobApplicationUpdateViewPort.createNewJobApplication(new JobApplicationView(eventWithMetadata));
    }

    @Override
    public void deleteJobApplication(RecordedDomainEventWithMetadata<JobApplicationDeleted> eventWithMetadata) {
        jobApplicationUpdateViewPort.deleteJobApplication(eventWithMetadata.data().id());
    }

    @Override
    public void updateJobOfferData(RecordedDomainEventWithMetadata<JobApplicationJobOfferUpdated> eventWithMetadata) {
        jobApplicationUpdateViewPort.updateJobOfferData(
            eventWithMetadata.data().id(), eventWithMetadata.data().title(), eventWithMetadata.metaData()
        );
    }

    @Override
    public void updateCompanyName(RecordedDomainEventWithMetadata<JobApplicationCompanyNameUpdated> eventWithMetadata) {
        jobApplicationUpdateViewPort.updateCompanyName(
            eventWithMetadata.data().id(), eventWithMetadata.data().name(), eventWithMetadata.metaData()
        );
    }

    @Override
    public void updateStatus(RecordedDomainEventWithMetadata<JobApplicationStatusChanged> eventWithMetadata) {
        jobApplicationUpdateViewPort.updateStatus(
            eventWithMetadata.data().id(), eventWithMetadata.data().status(), eventWithMetadata.metaData()
        );
    }

    @Override
    public void updateContent(RecordedDomainEventWithMetadata<JobApplicationContentChanged> eventWithMetadata) {
        jobApplicationUpdateViewPort.updateContent(
            eventWithMetadata.data().id(), eventWithMetadata.data().content(), eventWithMetadata.metaData()
        );
    }


}

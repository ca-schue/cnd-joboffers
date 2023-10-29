package thi.cnd.careerservice.jobapplication.view.update;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationContentChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationCreated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationDeleted;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationStatusChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationCompanyNameUpdated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import jakarta.validation.constraints.NotNull;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

/**
 * Applies domain events to the job application view to mirror the current state of the aggregate data
 */
@Validated
public interface UpdateJobApplicationViewService {

    @NotNull void newJobApplicationCreated(@NotNull RecordedDomainEventWithMetadata<JobApplicationCreated> eventWithMetadata);

    @NotNull void deleteJobApplication(@NotNull RecordedDomainEventWithMetadata<JobApplicationDeleted> eventWithMetadata);

    @NotNull void updateJobOfferData(@NotNull RecordedDomainEventWithMetadata<JobApplicationJobOfferUpdated> eventWithMetadata);

    @NotNull void updateCompanyName(@NotNull RecordedDomainEventWithMetadata<JobApplicationCompanyNameUpdated> eventWithMetadata);

    @NotNull void updateStatus(@NotNull RecordedDomainEventWithMetadata<JobApplicationStatusChanged> eventWithMetadata);

    @NotNull void updateContent(@NotNull RecordedDomainEventWithMetadata<JobApplicationContentChanged> eventWithMetadata);
}

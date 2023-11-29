package thi.cnd.careerservice.jobapplication.query.port;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationContentChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationCreated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationDeleted;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationStatusChanged;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationCompanyNameUpdated;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent.JobApplicationViewOnlyEvent.JobApplicationJobOfferUpdated;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import jakarta.validation.constraints.NotNull;

/**
 * Applies domain events to the job application view to mirror the current state of the aggregate data
 */
@Validated
public interface UpdateJobApplicationViewService {

    void newJobApplicationCreated(@NotNull RecordedDomainEventWithMetadata<JobApplicationCreated> eventWithMetadata);

    void deleteJobApplication(@NotNull RecordedDomainEventWithMetadata<JobApplicationDeleted> eventWithMetadata);

    void updateJobOfferData(@NotNull RecordedDomainEventWithMetadata<JobApplicationJobOfferUpdated> eventWithMetadata);

    void updateCompanyName(@NotNull RecordedDomainEventWithMetadata<JobApplicationCompanyNameUpdated> eventWithMetadata);

    void updateStatus(@NotNull RecordedDomainEventWithMetadata<JobApplicationStatusChanged> eventWithMetadata);

    void updateContent(@NotNull RecordedDomainEventWithMetadata<JobApplicationContentChanged> eventWithMetadata);
}

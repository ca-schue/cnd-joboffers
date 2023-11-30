package thi.cnd.careerservice.jobapplication.command.port;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;

public interface JobApplicationCrossAggregateEventHandler {
    void deleteJobApplication(JobOfferId id);

    void applyJobOfferStatusChanged(JobOfferId id, JobOfferStatus status);

    void updateJobOfferTitle(JobOfferId id, String title);
}

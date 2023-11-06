package thi.cnd.careerservice.jobapplication.domain;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;

public interface JobApplicationCrossAggregateEventHandler {
    void deleteJobApplication(JobOfferId id);

    void deleteJobApplication(UserId userId);

    void applyJobOfferStatusChanged(JobOfferId id, JobOfferStatus status);

    void updateJobOfferTitle(JobOfferId id, String title);

    void updateCompanyName(CompanyId id, String name);
}

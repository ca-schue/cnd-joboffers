package thi.cnd.careerservice.jobapplication.query.port;

import java.util.List;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobApplicationQueryPort {

    /**
     * Get job application for id if etag is lower or equal than current document version
     */
    @NotNull JobApplicationView getJobApplication(@NotNull JobApplicationId id, @NotNull Optional<ETag> eTag);

    /**
     * Get all published job applications for a specific job offer
     */
    @NotNull List<JobApplicationView> getAllPublishedJobApplicationsByJobOffer(@NotNull JobOfferId jobOfferId);

    /**
     * Get job application for id if etag is lower or equal than current document version
     */
    List<JobApplicationView> getAllJobApplicationsForJobOfferWithStatus(JobOfferId jobOfferId, List<JobApplicationStatus> statuses);

    /**
     * Get all job applications for company
     */
    @NotNull List<JobApplicationView> getAllJobApplicationsForCompanyId(@NotNull CompanyId id);

    /**
     * Get all job applications for user
     */
    @NotNull List<JobApplicationView> getAllJobApplicationsForUserId(@NotNull UserId userId);

    /**
     * Count all job applications for user id
     */
    long countAllJobApplicationsForUserId(@NotNull UserId userId);

    /**
     * Get job application for user and job offer if etag is lower or equal than current document version
     */
    @NotNull JobApplicationView getJobApplicationByUserAndJobOffer(UserId userId, JobOfferId jobOfferId, Optional<ETag> eTag);

}

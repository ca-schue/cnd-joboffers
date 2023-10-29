package thi.cnd.careerservice.jobapplication.view.query;

import java.util.List;
import java.util.Optional;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobApplicationQueryService {

    /**
     * Get job application for id if etag is lower or equal than current document version
     */
    @NotNull JobApplicationView getJobApplication(@NotNull JobApplicationId id, @NotNull Optional<ETag> eTag);

    /**
     * Get all job application for user
     */
    @NotNull List<JobApplicationView> getAllJobApplicationsByUser(UserId userId);

    /**
     * Get published job application for a job offer
     */
    @NotNull List<JobApplicationView> getAllPublishedJobApplicationsByJobOffer(JobOfferId jobOfferId);

    /**
     * Get job application for user and a specific job offer
     */
    @NotNull JobApplicationView getJobApplicationByUserAndJobOffer(@NotNull UserId userId, @NotNull JobOfferId jobOfferId, @NotNull Optional<ETag> eTag);
}

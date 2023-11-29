package thi.cnd.careerservice.jobapplication.query.application.serivce;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryService;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultJobApplicationQueryService implements JobApplicationQueryService {

    private final JobApplicationQueryPort port;

    @Override
    public JobApplicationView getJobApplication(JobApplicationId id, Optional<ETag> eTag) {
        return port.getJobApplication(id, eTag);
    }

    @Override
    public List<JobApplicationView> getAllJobApplicationsByUser(UserId userId) {
        return port.getAllJobApplicationsForUserId(userId);
    }

    @Override
    public List<JobApplicationView> getAllPublishedJobApplicationsByJobOffer(JobOfferId jobOfferId) {
        return port.getAllPublishedJobApplicationsByJobOffer(jobOfferId);
    }

    @Override
    public JobApplicationView getJobApplicationByUserAndJobOffer(UserId userId, JobOfferId jobOfferId, Optional<ETag> eTag) {
        return port.getJobApplicationByUserAndJobOffer(userId, jobOfferId, eTag);
    }


}

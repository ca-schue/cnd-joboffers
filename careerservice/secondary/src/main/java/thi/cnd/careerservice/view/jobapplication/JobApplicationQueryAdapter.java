package thi.cnd.careerservice.view.jobapplication;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationView;
import thi.cnd.careerservice.jobapplication.view.query.JobApplicationQueryPort;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.view.jobapplication.mapper.JobApplicationViewDaoMapper;
import thi.cnd.careerservice.view.jobapplication.model.JobApplicationViewDAO;
import thi.cnd.careerservice.view.jobapplication.repository.JobApplicationViewRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JobApplicationQueryAdapter implements JobApplicationQueryPort {

    private final JobApplicationViewRepository repository;

    @Override
    public JobApplicationView getJobApplication(JobApplicationId id, Optional<ETag> eTag) {
        Optional<JobApplicationViewDAO> searchedDao = repository.findById(id);

        var dao = searchedDao.orElseThrow(() -> new ViewResourceNotFoundException("JobApplication", id));

        validateETagVersion(eTag, dao);

        return JobApplicationViewDaoMapper.toJobApplication(dao);
    }

    @Override
    public List<JobApplicationView> getAllPublishedJobApplicationsByJobOffer(JobOfferId jobOfferId) {
        return repository.findAllByJobOfferIdAndStatusIsNot(jobOfferId, JobApplicationStatus.DRAFT)
            .stream().map(JobApplicationViewDaoMapper::toJobApplication)
            .toList();
    }

    @Override
    public List<JobApplicationView> getAllJobApplicationsForJobOfferWithStatus(JobOfferId jobOfferId, List<JobApplicationStatus> statuses) {
        return repository.findAllByJobOfferIdAndStatusIsIn(jobOfferId, statuses)
            .stream().map(JobApplicationViewDaoMapper::toJobApplication)
            .toList();
    }

    @Override
    public List<JobApplicationView> getAllJobApplicationsForCompanyId(CompanyId id) {
        return repository.findAllByCompanyId(id)
            .stream().map(JobApplicationViewDaoMapper::toJobApplication)
            .toList();
    }

    @Override
    public List<JobApplicationView> getAllJobApplicationsForUserId(UserId userId) {
        return repository.findAllByUserId(userId)
            .stream().map(JobApplicationViewDaoMapper::toJobApplication)
            .toList();
    }

    @Override
    public long countAllJobApplicationsForUserId(UserId userId) {
        return repository.countAllByUserId(userId);
    }

    @Override
    public JobApplicationView getJobApplicationByUserAndJobOffer(UserId userId, JobOfferId jobOfferId, Optional<ETag> eTag) {
        var searchedDao = repository.findByUserIdAndJobOfferId(userId, jobOfferId);

        var dao = searchedDao.orElseThrow(() -> new ViewResourceNotFoundException("JobApplication"));

        validateETagVersion(eTag, dao);

        return JobApplicationViewDaoMapper.toJobApplication(dao);
    }

    private static void validateETagVersion(Optional<ETag> eTag, JobApplicationViewDAO dao) {
        if (eTag.isPresent()) {
            var requiredVersion = eTag.get().getValue();
            if (dao.metadata().version() <= requiredVersion) {
                throw new IdentifiedRuntimeException(HttpStatus.NOT_MODIFIED);
            }
        }
    }

}

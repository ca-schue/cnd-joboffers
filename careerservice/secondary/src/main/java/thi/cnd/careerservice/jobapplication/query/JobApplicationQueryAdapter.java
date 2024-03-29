package thi.cnd.careerservice.jobapplication.query;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.jobapplication.query.mapper.JobApplicationViewDaoMapper;
import thi.cnd.careerservice.jobapplication.query.model.JobApplicationViewDAO;
import thi.cnd.careerservice.jobapplication.query.repository.JobApplicationViewRepository;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ViewResourceNotFoundException;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryPort;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import lombok.RequiredArgsConstructor;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_MODIFIED;

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
                throw new IdentifiedRuntimeException(RESOURCE_NOT_MODIFIED);
            }
        }
    }

}

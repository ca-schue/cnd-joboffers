package thi.cnd.careerservice.view.jobapplication.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.view.jobapplication.model.JobApplicationViewDAO;

public interface JobApplicationViewRepository
    extends MongoRepository<JobApplicationViewDAO, JobApplicationId>, JobApplicationComplexQueryViewRepository {

    Optional<JobApplicationViewDAO> deleteOneById(JobApplicationId id);

    Optional<JobApplicationViewDAO> findByUserIdAndJobOfferId(UserId userId, JobOfferId jobOfferId);

    List<JobApplicationViewDAO> findAllByJobOfferIdAndStatusIsNot(JobOfferId jobOfferId, JobApplicationStatus status);

    List<JobApplicationViewDAO> findAllByJobOfferIdAndStatusIsIn(JobOfferId jobOfferId, List<JobApplicationStatus> status);

    List<JobApplicationViewDAO> findAllByCompanyId(CompanyId id);

    List<JobApplicationViewDAO> findAllByUserId(UserId userId);

    long countAllByUserId(UserId userId);
}

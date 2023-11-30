package thi.cnd.careerservice.jobapplication.query.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.jobapplication.query.model.JobApplicationViewDAO;

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

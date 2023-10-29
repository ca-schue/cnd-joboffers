package thi.cnd.careerservice.view.joboffer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.view.joboffer.model.JobOfferViewDAO;

public interface JobOfferViewRepository extends MongoRepository<JobOfferViewDAO, JobOfferId>, JobOfferComplexQueryRepository {

    Optional<JobOfferViewDAO> deleteOneById(JobOfferId id);

}

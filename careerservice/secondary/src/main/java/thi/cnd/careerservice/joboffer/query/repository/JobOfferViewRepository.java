package thi.cnd.careerservice.joboffer.query.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.model.JobOfferViewDAO;

public interface JobOfferViewRepository extends MongoRepository<JobOfferViewDAO, JobOfferId>, JobOfferComplexQueryRepository {

    Optional<JobOfferViewDAO> deleteOneById(JobOfferId id);

}

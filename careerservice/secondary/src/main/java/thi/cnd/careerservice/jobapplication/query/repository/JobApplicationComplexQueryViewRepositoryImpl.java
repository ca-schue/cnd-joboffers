package thi.cnd.careerservice.jobapplication.query.repository;

import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.query.model.JobApplicationViewDAO;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JobApplicationComplexQueryViewRepositoryImpl implements JobApplicationComplexQueryViewRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<JobApplicationViewDAO> updateJobOfferData(JobApplicationId id, String title, ViewEventMetadata viewEventMetadata) {
        var query = new Query(Criteria.where("id").is(id));
        var update = Update.update("jobOffer.title", title).set("metadata", viewEventMetadata);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, JobApplicationViewDAO.class));
    }

    @Override
    public Optional<JobApplicationViewDAO> updateCompanyName(JobApplicationId id, String name, ViewEventMetadata viewEventMetadata) {
        var query = new Query(Criteria.where("id").is(id));
        var update = Update.update("company.name", name).set("metadata", viewEventMetadata);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, JobApplicationViewDAO.class));
    }

    @Override
    public Optional<JobApplicationViewDAO> updateStatus(JobApplicationId id, JobApplicationStatus status, ViewEventMetadata viewEventMetadata) {
        var query = new Query(Criteria.where("id").is(id));
        var update = Update.update("status", status).set("metadata", viewEventMetadata);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, JobApplicationViewDAO.class));
    }

    @Override
    public Optional<JobApplicationViewDAO> updateContent(JobApplicationId id, String content, ViewEventMetadata viewEventMetadata) {
        var query = new Query(Criteria.where("id").is(id));
        var update = Update.update("content", content).set("metadata", viewEventMetadata);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, JobApplicationViewDAO.class));
    }


}

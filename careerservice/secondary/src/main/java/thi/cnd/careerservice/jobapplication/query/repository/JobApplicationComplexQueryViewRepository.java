package thi.cnd.careerservice.jobapplication.query.repository;

import java.util.Optional;

import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.jobapplication.query.model.JobApplicationViewDAO;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;

public interface JobApplicationComplexQueryViewRepository {
    Optional<JobApplicationViewDAO> updateJobOfferData(JobApplicationId id, String title, ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateCompanyName(JobApplicationId id, String name, ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateStatus(JobApplicationId id, JobApplicationStatus status,
        ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateContent(JobApplicationId id, String content, ViewEventMetadata viewEventMetadata);
}

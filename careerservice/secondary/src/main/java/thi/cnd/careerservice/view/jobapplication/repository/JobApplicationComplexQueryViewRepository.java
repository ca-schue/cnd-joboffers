package thi.cnd.careerservice.view.jobapplication.repository;

import java.util.Optional;

import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import thi.cnd.careerservice.view.jobapplication.model.JobApplicationViewDAO;

public interface JobApplicationComplexQueryViewRepository {
    Optional<JobApplicationViewDAO> updateJobOfferData(JobApplicationId id, String title, ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateCompanyName(JobApplicationId id, String name, ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateStatus(JobApplicationId id, JobApplicationStatus status,
        ViewEventMetadata viewEventMetadata);

    Optional<JobApplicationViewDAO> updateContent(JobApplicationId id, String content, ViewEventMetadata viewEventMetadata);
}

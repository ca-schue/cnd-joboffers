package thi.cnd.careerservice.command.jobapplication;

import java.util.UUID;

import org.springframework.stereotype.Component;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.shared.DataWithVersion;
import thi.cnd.careerservice.user.model.UserId;

@Component
public class JobApplicationFixture {

    private final JobApplicationCommandApiTestAdapter careerServiceAdapter;

    private JobApplicationFixture(JobApplicationCommandApiTestAdapter careerServiceAdapter) {
        this.careerServiceAdapter = careerServiceAdapter;
    }

    public static class Builder {

        private JobApplicationId id = new JobApplicationId();
        private UserId userId = new UserId();

        private JobOfferId jobOfferId = new JobOfferId();
        private String jobOfferTitle = "ANY";

        private CompanyId companyId = new CompanyId();
        private String companyName = "ANY";
        private String companyLocation = "ANY";

        private JobApplicationStatus status = JobApplicationStatus.OPEN;
        private String content = "ANY";

        private final JobApplicationCommandApiTestAdapter careerServiceAdapter;

        public Builder(JobApplicationCommandApiTestAdapter careerServiceAdapter) {
            this.careerServiceAdapter = careerServiceAdapter;
        }

        public Builder id(UUID id) {
            this.id = new JobApplicationId(id);
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = new UserId(userId);
            return this;
        }

        public Builder jobOfferId(UUID jobOfferId) {
            this.jobOfferId = new JobOfferId(jobOfferId);
            return this;
        }

        public Builder jobOfferTitle(String jobOfferTitle) {
            this.jobOfferTitle = jobOfferTitle;
            return this;
        }

        public Builder companyId(UUID companyId) {
            this.companyId = new CompanyId(companyId);
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder companyLocation(String companyLocation) {
            this.companyLocation = companyLocation;
            return this;
        }

        public Builder status(JobApplicationStatus status) {
            this.status = status;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public DataWithVersion<JobApplication> build() {
            return this.careerServiceAdapter.create(this);
        }

        public JobApplicationId getId() {
            return id;
        }

        public UserId getUserId() {
            return userId;
        }

        public JobOfferId getJobOfferId() {
            return jobOfferId;
        }

        public String getJobOfferTitle() {
            return jobOfferTitle;
        }

        public CompanyId getCompanyId() {
            return companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getCompanyLocation() {
            return companyLocation;
        }

        public JobApplicationStatus getStatus() {
            return status;
        }

        public String getContent() {
            return content;
        }
    }

    public Builder create() {
        return new Builder(careerServiceAdapter);
    }

    public Builder createDraft() {
        return new Builder(careerServiceAdapter).status(JobApplicationStatus.DRAFT);
    }


}

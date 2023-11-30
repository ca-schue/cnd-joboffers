package thi.cnd.careerservice.command.joboffer;

import java.util.List;
import java.util.UUID;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.SalaryRange;
import thi.cnd.careerservice.shared.DataWithVersion;
import thi.cnd.careerservice.user.model.UserId;

@Component
public class JobOfferFixture {

    private final JobOfferCommandApiTestAdapter careerServiceAdapter;

    private JobOfferFixture(JobOfferCommandApiTestAdapter careerServiceAdapter) {
        this.careerServiceAdapter = careerServiceAdapter;
    }

    public static class Builder {
        private CompanyId companyId = new CompanyId();
        private UserId createdBy = new UserId();
        private String title = "ANY";
        private String description = "ANY";
        private JobOfferStatus status = JobOfferStatus.OPEN;
        private List<String> tags = List.of("Tag1");
        private SalaryRange salaryRange = new SalaryRange(Money.of(50000, "EUR"), Money.of(100000, "EUR"));

        private final JobOfferCommandApiTestAdapter careerServiceAdapter;

        public Builder(JobOfferCommandApiTestAdapter careerServiceAdapter) {
            this.careerServiceAdapter = careerServiceAdapter;
        }

        public Builder companyId(UUID uuid) {
            this.companyId = new CompanyId(uuid);
            return this;
        }

        public Builder createdBy(UUID uuid) {
            this.createdBy = new UserId(uuid);
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(JobOfferStatus status) {
            this.status = status;
            return this;
        }

        public Builder title(String ...tags) {
            this.tags = List.of(tags);
            return this;
        }

        public Builder salaryRange(int lowerBound, int upperBound) {
            this.salaryRange = new SalaryRange(
                Money.of(lowerBound, "EUR"),
                Money.of(upperBound, "EUR")
            );
            return this;
        }

        public DataWithVersion<JobOffer> build() {
            return this.careerServiceAdapter.create(this);
        }

        public CompanyId getCompanyId() {
            return companyId;
        }

        public UserId getCreatedBy() {
            return createdBy;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public JobOfferStatus getStatus() {
            return status;
        }

        public List<String> getTags() {
            return tags;
        }

        public SalaryRange getSalaryRange() {
            return salaryRange;
        }
    }

    public Builder create() {
        return new Builder(careerServiceAdapter);
    }

    public Builder createDraft() {
        return new Builder(careerServiceAdapter).status(JobOfferStatus.DRAFT);
    }


}

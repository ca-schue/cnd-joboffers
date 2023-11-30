package thi.cnd.careerservice.jobapplication.query.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import jakarta.validation.constraints.NotBlank;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

@Validated
public record JobApplicationCompanyView(
    @Indexed(background = true) CompanyId id,
    @NotBlank String name,
    @NotBlank String location
) {

    public JobApplicationCompanyView(JobApplicationEvent.JobApplicationCreated.JobApplicationCreatedCompanyData companyData) {
        this(companyData.id(), companyData.name(), companyData.location());
    }
}

package thi.cnd.careerservice.joboffer.query.model;

import java.util.List;

import javax.annotation.Nullable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.SalaryRange;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@Document(collection = "JobOfferView")
public record JobOfferViewDAO(
    @Id JobOfferId id,
    @NotNull CompanyId companyId,
    @NotNull UserId createdBy,
    @NotBlank String title,
    @NotBlank String description,
    @NotNull JobOfferStatus status,
    @NotNull List<String> tags,
    @Nullable SalaryRange salaryRange,
    @NotNull ViewEventMetadata metadata
) {

}

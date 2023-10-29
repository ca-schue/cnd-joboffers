package thi.cnd.careerservice.view.jobapplication.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationCompanyView;
import thi.cnd.careerservice.jobapplication.view.model.JobApplicationJobOfferView;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public record JobApplicationViewDAO(
    @Id JobApplicationId id,
    @NotNull JobApplicationCompanyView company,
    @NotNull JobApplicationJobOfferView jobOffer,
    @Indexed(background = true) UserId userId,
    @Indexed(background = true) JobApplicationStatus status,
    @NotBlank String content,
    @NotNull ViewEventMetadata metadata
) {
}

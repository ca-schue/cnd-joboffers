package thi.cnd.careerservice.jobapplication.query.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import jakarta.validation.constraints.NotBlank;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;

@Validated
public record JobApplicationJobOfferView(
    @Indexed(background = true) JobOfferId id,
    @NotBlank String title
) {

    public JobApplicationJobOfferView(JobApplicationEvent.JobApplicationCreated.JobApplicationJobOfferData view) {
        this(view.id(), view.title());
    }

}

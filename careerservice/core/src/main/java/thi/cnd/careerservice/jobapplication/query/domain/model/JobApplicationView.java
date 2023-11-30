package thi.cnd.careerservice.jobapplication.query.domain.model;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.shared.view.model.View;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Validated
@AllArgsConstructor
public class JobApplicationView implements View {

    @NotNull private final JobApplicationId id;
    @NotNull private JobApplicationCompanyView company;
    @NotNull private JobApplicationJobOfferView jobOffer;
    @NotNull private final UserId userId;
    @NotNull private JobApplicationStatus status;
    @NotBlank private String content;
    @NotNull private ViewEventMetadata metadata;

    public JobApplicationView(RecordedDomainEventWithMetadata<JobApplicationEvent.JobApplicationCreated> event) {
        this(
            event.data().id(),
            new JobApplicationCompanyView(event.data().companyData()),
            new JobApplicationJobOfferView(event.data().jobOffer()),
            event.data().userId(),
            event.data().status(),
            event.data().content(),
            new ViewEventMetadata(event.metaData())
        );
    }

}

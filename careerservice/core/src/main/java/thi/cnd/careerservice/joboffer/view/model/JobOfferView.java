package thi.cnd.careerservice.joboffer.view.model;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.model.SalaryRange;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.shared.view.model.View;
import thi.cnd.careerservice.shared.view.model.ViewEventMetadata;
import thi.cnd.careerservice.user.model.UserId;

@Data
@Validated
@AllArgsConstructor
public class JobOfferView implements View {

    private final @NotNull JobOfferId id;
    private @NotNull CompanyId companyId;
    private @NotNull UserId createdBy;
    private @NotBlank String title;
    private @NotBlank String description;
    private @NotNull JobOfferStatus status;
    private @NotNull List<String> tags;
    private @Nullable SalaryRange salaryRange;
    private @NotNull ViewEventMetadata metadata;

    public JobOfferView(RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferCreated> event) {
        this(
            event.data().id(),
            event.data().companyId(),
            event.data().createdBy(),
            event.data().title(),
            event.data().description(),
            event.data().status(),
            event.data().tags(),
            event.data().salaryRange(),
            new ViewEventMetadata(event.metaData())
        );
    }

}

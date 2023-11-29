package thi.cnd.careerservice.joboffer.query.application.service;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.event.model.RecordedDomainEventWithMetadata;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import jakarta.validation.constraints.NotNull;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;

@Validated
public interface UpdateJobOfferViewService {
    @NotNull
    JobOfferView newJobOfferCreated(@NotNull RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferCreated> event);

    @NotNull JobOfferView updateJobOfferStatus(@NotNull RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferStatusUpdated> event);

    @NotNull JobOfferView updateJobOfferTitle(@NotNull RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferTitleChanged> event);

    @NotNull JobOfferView updateJobOfferAttributes(@NotNull RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferAttributesChanged> event);

    @NotNull JobOfferView deleteJobOffer(@NotNull RecordedDomainEventWithMetadata<JobOfferEvent.JobOfferDeleted> event);

}

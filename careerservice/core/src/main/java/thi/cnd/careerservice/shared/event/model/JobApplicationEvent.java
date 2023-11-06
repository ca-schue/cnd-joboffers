package thi.cnd.careerservice.shared.event.model;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.model.JobApplicationStatus;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.shared.event.RegisteredDomainEvent;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public sealed interface JobApplicationEvent extends DomainEvent {

    @RegisteredDomainEvent(name = "JobApplicationCreated")
    record JobApplicationCreated(
        @NotNull JobApplicationId id,
        @NotNull UserId userId,
        @NotNull JobApplicationJobOfferData jobOffer,
        @NotNull JobApplicationCreatedCompanyData companyData,
        @NotNull JobApplicationStatus status,
        @NotBlank String content
    ) implements JobApplicationEvent {
        public record JobApplicationCreatedCompanyData(
            @NotNull CompanyId id,
            @NotBlank String name,
            @NotBlank String location
        ) {
        }

        public record JobApplicationJobOfferData(
            @NotNull JobOfferId id,
            @NotBlank String title
        ) {
            public JobApplicationJobOfferData(JobOfferView jobOffer) {
                this(jobOffer.getId(), jobOffer.getTitle());
            }
        }
    }

    @RegisteredDomainEvent(name = "JobApplicationDeleted")
    record JobApplicationDeleted(
        @NotNull JobApplicationId id
    ) implements JobApplicationEvent, DeletionEvent {
    }

    @RegisteredDomainEvent(name = "JobApplicationStatusChanged")
    record JobApplicationStatusChanged(
        @NotNull JobApplicationId id,
        @NotBlank JobApplicationStatus status
    ) implements JobApplicationEvent {
    }

    @RegisteredDomainEvent(name = "JobApplicationContentChanged")
    record JobApplicationContentChanged(
        @NotNull JobApplicationId id,
        @NotBlank String content
    ) implements JobApplicationEvent {
    }

    sealed interface JobApplicationViewOnlyEvent extends JobApplicationEvent, ViewOnlyEvent {

        @RegisteredDomainEvent(name = "JobApplicationJobOfferUpdated")
        record JobApplicationJobOfferUpdated(
            @NotNull JobApplicationId id,
            @NotBlank String title
        ) implements JobApplicationViewOnlyEvent {
        }

        @RegisteredDomainEvent(name = "JobApplicationCompanyNameUpdated")
        record JobApplicationCompanyNameUpdated(
            @NotNull JobApplicationId id,
            @NotBlank String name
        ) implements JobApplicationViewOnlyEvent {
        }

    }


}

package thi.cnd.careerservice.shared.event.model;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.shared.event.RegisteredDomainEvent;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.SalaryRange;

/**
 * Contains all internal job offer domain events
 */
@Validated
public sealed interface JobOfferEvent extends DomainEvent {

    @RegisteredDomainEvent(name = "JobOfferCreated")
    record JobOfferCreated(
        JobOfferId id,
        CompanyId companyId,
        UserId createdBy,
        String title,
        String description,
        JobOfferStatus status,
        List<String> tags,
        SalaryRange salaryRange
    ) implements JobOfferEvent {
    }

    @RegisteredDomainEvent(name = "JobOfferAttributesChanged")
    record JobOfferAttributesChanged(
        JobOfferId id,
        String description,
        List<String> tags,
        SalaryRange salaryRange
    ) implements JobOfferEvent {
    }

    @RegisteredDomainEvent(name = "JobOfferTitleChanged")
    record JobOfferTitleChanged(
        JobOfferId id,
        String title
    ) implements JobOfferEvent {
    }

    @RegisteredDomainEvent(name = "JobOfferStatusUpdated")
    record JobOfferStatusUpdated(
        JobOfferId id,
        JobOfferStatus status
    ) implements JobOfferEvent {
    }

    @RegisteredDomainEvent(name = "JobOfferDeleted")
    record JobOfferDeleted(
        JobOfferId id
    ) implements JobOfferEvent, DeletionEvent {
    }

}

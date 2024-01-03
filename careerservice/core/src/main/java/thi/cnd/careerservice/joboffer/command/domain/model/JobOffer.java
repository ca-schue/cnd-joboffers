package thi.cnd.careerservice.joboffer.command.domain.model;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.shared.AggregateRoot;
import thi.cnd.careerservice.shared.event.model.JobOfferEvent;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ResourceDisallowsModificationException;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static thi.cnd.careerservice.exception.BasicErrorCode.CONFLICTING_ACTION;
import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_MODIFIED;

@Getter
@Validated
public class JobOffer extends AggregateRoot<JobOfferId, JobOffer, JobOfferEvent> {

    @NotNull
    private JobOfferId id;
    @NotNull
    private CompanyId companyId;
    @NotNull
    private UserId createdBy;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private JobOfferStatus status;
    @NotNull
    private List<String> tags;
    @Nullable
    private SalaryRange salaryRange;

    private JobOffer() {}

    public static JobOffer init() {
        return new JobOffer();
    }

    public JobOffer create(
        @NotNull CompanyId companyId,
        @NotNull UserId createdBy,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull JobOfferStatus status,
        @NotNull List<String> tags,
        @Nullable SalaryRange salaryRange
    ) {
        return fire(
            new JobOfferEvent.JobOfferCreated(
                new JobOfferId(), companyId, createdBy, title, description, status, tags, salaryRange
            )
        );
    }

    public void setStatus(@NotNull JobOfferStatus newStatus) {
        validateModification();

        if (status == newStatus) {
            throw new IdentifiedRuntimeException(RESOURCE_NOT_MODIFIED);
        }

        if (newStatus == JobOfferStatus.DELETED) {
            throw new IllegalArgumentException("Cannot update status to DELETED. Use delete() instead.");
        }

        if (newStatus == JobOfferStatus.DRAFT) {
            throw new IdentifiedRuntimeException(CONFLICTING_ACTION,
                () -> "Cannot return the status back to draft once the job offer was published");
        }

        fire(new JobOfferEvent.JobOfferStatusUpdated(id, newStatus));
    }

    public void setTitle(String title) {
        validateModification();

        if (!this.title.equals(title)) {
            fire(new JobOfferEvent.JobOfferTitleChanged(id, title));
        }
    }

    public void setAttributes(String description, List<String> tags, SalaryRange salaryRange) {
        validateModification();

        if (!this.description.equals(description) || !this.tags.equals(tags) || !Objects.equals(this.salaryRange, salaryRange)) {
            fire(new JobOfferEvent.JobOfferAttributesChanged(id, description, tags, salaryRange));
        }
    }

    public void delete() {
        if (isDeleted()) {
            throw new ResourceDisallowsModificationException(id);
        }

        fire(new JobOfferEvent.JobOfferDeleted(id));
    }

    @Override
    public void apply(JobOfferEvent jobOfferEvent) {
        switch (jobOfferEvent) {
            case JobOfferEvent.JobOfferCreated event -> applyCreateEvent(event);
            case JobOfferEvent.JobOfferStatusUpdated event -> applyStatus(event);
            case JobOfferEvent.JobOfferAttributesChanged event -> applyAttributesChanges(event);
            case JobOfferEvent.JobOfferTitleChanged event -> applyTitle(event);
            case JobOfferEvent.JobOfferDeleted __ -> applyDelete();
        };
    }

    private void applyCreateEvent(JobOfferEvent.JobOfferCreated event) {
        this.id = event.id();
        this.companyId = event.companyId();
        this.createdBy = event.createdBy();
        this.title = event.title();
        this.description = event.description();
        this.status = event.status();
        this.tags = event.tags();
        this.salaryRange = event.salaryRange();
    }

    private void applyStatus(JobOfferEvent.JobOfferStatusUpdated event) {
        this.status = event.status();
    }

    private void applyTitle(JobOfferEvent.JobOfferTitleChanged event) {
        this.title = event.title();
    }

    private void applyAttributesChanges(JobOfferEvent.JobOfferAttributesChanged event) {
        this.description = event.description();
        this.tags = event.tags();
        this.salaryRange = event.salaryRange();
    }

    private void applyDelete() {
        this.status = JobOfferStatus.DELETED;
    }

    @Override
    public boolean isDeleted() {
        return status == JobOfferStatus.DELETED;
    }

    public boolean allowsModification() {
        return !isDeleted() && status != JobOfferStatus.CLOSED;
    }

    public void validateModification() {
        if (!allowsModification()) {
            throw new ResourceDisallowsModificationException(id);
        }
    }


}

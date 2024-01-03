package thi.cnd.careerservice.jobapplication.command.domain.model;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.shared.event.model.JobApplicationEvent;
import thi.cnd.careerservice.exception.IdentifiedRuntimeException;
import thi.cnd.careerservice.exception.ResourceDisallowsModificationException;
import thi.cnd.careerservice.shared.AggregateRoot;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.query.domain.model.JobOfferView;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import static thi.cnd.careerservice.exception.BasicErrorCode.CONFLICTING_ACTION;
import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_MODIFIED;

@Validated
@Getter
public class JobApplication extends AggregateRoot<JobApplicationId, JobApplication, JobApplicationEvent> {
    @NotNull
    private JobApplicationId id;
    @NotNull
    private UserId userId;
    @NotNull
    private CompanyId companyId;
    @NotNull
    private JobOfferId jobOfferId;
    @NotNull
    private JobApplicationStatus status;
    @NotBlank
    private String content;

    public static JobApplication init() {
        return new JobApplication();
    }

    public JobApplication create(
        @NotNull JobApplicationId jobApplicationId,
        @NotNull UserId userId,
        @NotNull JobOfferView jobOffer,
        @NotNull CompanyId companyId,
        @NotBlank String companyName,
        @NotBlank String companyLocation,
        @NotNull JobApplicationStatus status,
        @NotBlank String content
    ) {
        return fire(
            new JobApplicationEvent.JobApplicationCreated(
                jobApplicationId, userId,
                new JobApplicationEvent.JobApplicationCreated.JobApplicationJobOfferData(jobOffer),
                new JobApplicationEvent.JobApplicationCreated.JobApplicationCreatedCompanyData(companyId, companyName, companyLocation),
                status, content
            )
        );
    }

    public JobApplication setStatus(JobApplicationStatus status) {
        if (this.status == JobApplicationStatus.DELETED) {
            throw new IllegalArgumentException("Cannot update status for deleted JobApplication");
        }

        validateModification();

        if (this.status == status) {
            throw new IdentifiedRuntimeException(RESOURCE_NOT_MODIFIED);
        }

        if (status == JobApplicationStatus.DRAFT) {
            throw new IdentifiedRuntimeException(CONFLICTING_ACTION, () -> "Cannot put a published job application back into draft.");
        }

        return fire(new JobApplicationEvent.JobApplicationStatusChanged(id, status));
    }

    public void setContent(String content) {
        validateModification();

        fire(new JobApplicationEvent.JobApplicationContentChanged(id, content));
    }

    public JobApplication delete() {
        return fire(new JobApplicationEvent.JobApplicationDeleted(id));
    }

    @Override
    public void apply(JobApplicationEvent genericEvent) {
        switch (genericEvent) {
            case JobApplicationEvent.JobApplicationCreated event -> applyCreateEvent(event);
            case JobApplicationEvent.JobApplicationStatusChanged event -> applyStatusChange(event.status());
            case JobApplicationEvent.JobApplicationContentChanged event -> applyContentChange(event.content());
            case JobApplicationEvent.JobApplicationDeleted __ -> applyDeletionEvent();
            case JobApplicationEvent.JobApplicationViewOnlyEvent __ -> {}
        };
    }

    private void applyStatusChange(JobApplicationStatus status) {
        this.status = status;
    }

    private void applyContentChange(String content) {
        this.content = content;
    }

    private void applyDeletionEvent() {
        this.status = JobApplicationStatus.DELETED;
    }

    private void applyCreateEvent(JobApplicationEvent.JobApplicationCreated event) {
        this.id = event.id();
        this.userId = event.userId();
        this.companyId = event.companyData().id();
        this.jobOfferId = event.jobOffer().id();
        this.status = event.status();
        this.content = event.content();
    }

    @Override
    public boolean isDeleted() {
        return status == JobApplicationStatus.DELETED;
    }

    @Override
    public JobApplicationId getId() {
        return id;
    }

    private boolean allowsModification() {
        return this.status == JobApplicationStatus.DRAFT || this.status == JobApplicationStatus.OPEN;
    }

    void validateModification() {
        if (!allowsModification()) {
            throw new ResourceDisallowsModificationException(id);
        }
    }

}

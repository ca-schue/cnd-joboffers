package thi.cnd.careerservice.jobapplication.command.application.model;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public sealed interface JobApplicationCommand {

    record CreateJobApplication(
        @NotNull UserId userId,
        @NotNull CompanyId companyId,
        @NotNull JobOfferId jobOfferId,
        @NotNull JobApplicationStatus status,
        @NotBlank String content
    ) implements JobApplicationCommand {
    }

    record UpdateJobApplicationStatus(
        @NotNull JobApplicationId id,
        @NotNull JobApplicationStatus status
    ) implements JobApplicationCommand {

    }

    record UpdateJobApplicationAttributes(
        @NotNull JobApplicationId id,
        @NotBlank String content
    ) implements JobApplicationCommand {

    }

    record DeleteJobApplication(
        @NotNull JobApplicationId id
    ) implements JobApplicationCommand {

    }

}

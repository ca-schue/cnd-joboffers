package thi.cnd.careerservice.joboffer.command;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.model.JobOfferStatus;
import thi.cnd.careerservice.joboffer.model.SalaryRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
public sealed interface JobOfferCommand {

    record CreateJobOffer(
        @NotNull CompanyId companyId,
        @NotNull UserId createdBy,
        @NotNull JobOfferStatus status,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull List<String> tags,
        @Nullable SalaryRange salaryRange
    ) implements JobOfferCommand {
    }

    record UpdateJobOfferStatus(
        @NotNull JobOfferId id,
        @NotNull JobOfferStatus status
    ) implements JobOfferCommand {

    }

    record UpdateJobOfferAttributes(
        @NotNull JobOfferId id,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull List<String> tags,
        @Nullable SalaryRange salaryRange
    ) implements JobOfferCommand {

    }

    record DeleteJobOffer(
        @NotNull JobOfferId id
    ) implements JobOfferCommand {

    }

}

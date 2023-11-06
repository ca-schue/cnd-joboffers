package thi.cnd.careerservice.joboffer.command;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.joboffer.model.JobOffer;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobOfferCommandHandler {
    @NotNull DataWithETag<JobOffer> createJobOffer(@NotNull JobOfferCommand.CreateJobOffer command);

    @NotNull DataWithETag<JobOffer> updateJobOfferStatus(@NotNull JobOfferCommand.UpdateJobOfferStatus command, @NotNull ETag expectedRevision);

    @NotNull DataWithETag<JobOffer> updateJobOfferAttributes(@NotNull JobOfferCommand.UpdateJobOfferAttributes command, @NotNull ETag expectedRevision);

    @NotNull DataWithETag<JobOffer> deleteJobOffer(@NotNull JobOfferCommand.DeleteJobOffer command, @NotNull ETag expectedRevision);
}

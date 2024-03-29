package thi.cnd.careerservice.jobapplication.command.port;

import org.springframework.validation.annotation.Validated;

import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.CreateJobApplication;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.DeleteJobApplication;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.UpdateJobApplicationAttributes;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.UpdateJobApplicationStatus;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import jakarta.validation.constraints.NotNull;

@Validated
public interface JobApplicationCommandHandler {
    @NotNull DataWithETag<JobApplication> createJobApplication(@NotNull CreateJobApplication command);

    @NotNull DataWithETag<JobApplication> deleteJobApplication(@NotNull DeleteJobApplication command, @NotNull ETag expectedRevision);

    @NotNull DataWithETag<JobApplication> updateStatus(@NotNull UpdateJobApplicationStatus command, @NotNull ETag expectedRevision);

    @NotNull DataWithETag<JobApplication> updateStatus(@NotNull UpdateJobApplicationAttributes command, @NotNull ETag expectedRevision);
}

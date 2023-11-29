package thi.cnd.careerservice.http.jobapplication.command;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.api.generated.JobApplicationCommandApi;
import thi.cnd.careerservice.api.generated.model.JobApplicationCreationRequestDTO;
import thi.cnd.careerservice.api.generated.model.JobApplicationCreationResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobApplicationUpdateRequestDTO;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.UpdateJobApplicationAttributes;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.UpdateJobApplicationStatus;
import thi.cnd.careerservice.jobapplication.command.port.JobApplicationCommandHandler;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobApplicationCommandApiController implements JobApplicationCommandApi {

    private final JobApplicationCommandHandler commandHandler;
    private final JobApplicationCommandApiMapper mapper;

    @Override
    public ResponseEntity<JobApplicationCreationResponseDTO> createJobApplication(
        UUID userId, UUID jobOfferId, JobApplicationCreationRequestDTO requestDTO
    ) {
        var command = mapper.toCommand(new UserId(userId), new JobOfferId(jobOfferId), requestDTO);
        var response = commandHandler.createJobApplication(command);

        return ResponseEntity
            .ok()
            .eTag(response.getETag().getText())
            .body(new JobApplicationCreationResponseDTO(response.getData().getId().getId()));
    }

    @Override
    public ResponseEntity<Void> acceptJobApplication(UUID companyId, UUID jobOfferId, UUID jobApplicationId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobApplicationStatus(new JobApplicationId(jobApplicationId), JobApplicationStatus.ACCEPTED);
        var response = commandHandler.updateStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> denyJobApplication(UUID companyId, UUID jobOfferId, UUID jobApplicationId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobApplicationStatus(new JobApplicationId(jobApplicationId), JobApplicationStatus.DENIED);
        var response = commandHandler.updateStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> publishJobApplication(UUID companyId, UUID jobApplicationId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobApplicationStatus(new JobApplicationId(jobApplicationId), JobApplicationStatus.OPEN);
        var response = commandHandler.updateStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> updateJobApplication(
        UUID userId, UUID jobApplicationId, String ifMatch, JobApplicationUpdateRequestDTO requestDTO
    ) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobApplicationAttributes(new JobApplicationId(jobApplicationId), requestDTO.getContent());
        var response = commandHandler.updateStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> deletedJobApplication(UUID userId, UUID jobApplicationId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new JobApplicationCommand.DeleteJobApplication(new JobApplicationId(jobApplicationId));
        var response = commandHandler.deleteJobApplication(command, expectedRevision);
        return buildResponseEntity(response);
    }

    private ResponseEntity<Void> buildResponseEntity(DataWithETag<JobApplication> response) {
        return ResponseEntity
            .noContent()
            .eTag(response.getETag().getText())
            .build();
    }


}

package thi.cnd.careerservice.http.joboffer.command;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import thi.cnd.careerservice.shared.model.DataWithETag;
import thi.cnd.careerservice.api.generated.JobOfferCommandApi;
import thi.cnd.careerservice.api.generated.model.JobOfferCreationRequestDTO;
import thi.cnd.careerservice.api.generated.model.JobOfferCreationResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobOfferUpdateRequestDTO;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.joboffer.command.application.model.JobOfferCommand.DeleteJobOffer;
import thi.cnd.careerservice.joboffer.command.application.model.JobOfferCommand.UpdateJobOfferStatus;
import thi.cnd.careerservice.joboffer.command.port.JobOfferCommandHandler;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOffer;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferStatus;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class JobOfferCommandApiController implements JobOfferCommandApi {

    private final JobOfferCommandHandler jobOfferCommandHandler;
    private final JobOfferCommandApiMapper jobOfferCommandApiMapper;

    @Override
    public ResponseEntity<JobOfferCreationResponseDTO> createJobOffer(UUID userId, JobOfferCreationRequestDTO requestDTO) {
        var response = jobOfferCommandHandler.createJobOffer(jobOfferCommandApiMapper.toCommand(requestDTO));

        return ResponseEntity
            .ok()
            .eTag(response.getETag().getText())
            .body(new JobOfferCreationResponseDTO(response.getData().getId().getId()));
    }

    @Override
    public ResponseEntity<Void> deleteJobOffer(UUID companyId, UUID jobOfferId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new DeleteJobOffer(new JobOfferId(jobOfferId));
        var response = jobOfferCommandHandler.deleteJobOffer(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> publishJobOffer(UUID companyId, UUID jobOfferId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobOfferStatus(new JobOfferId(jobOfferId), JobOfferStatus.OPEN);
        var response = jobOfferCommandHandler.updateJobOfferStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> closeJobOffer(UUID companyId, UUID jobOfferId, String ifMatch) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = new UpdateJobOfferStatus(new JobOfferId(jobOfferId), JobOfferStatus.CLOSED);
        var response = jobOfferCommandHandler.updateJobOfferStatus(command, expectedRevision);
        return buildResponseEntity(response);
    }

    @Override
    public ResponseEntity<Void> updateJobOfferAttributes(UUID companyId, UUID jobOfferId, String ifMatch,
        JobOfferUpdateRequestDTO requestDTO) {
        ETag expectedRevision = ETag.fromIfMatchHeader(ifMatch);
        var command = jobOfferCommandApiMapper.toCommand(new JobOfferId(jobOfferId), requestDTO);
        var response = jobOfferCommandHandler.updateJobOfferAttributes(command, expectedRevision);
        return buildResponseEntity(response);
    }

    private ResponseEntity<Void> buildResponseEntity(DataWithETag<JobOffer> response) {
        return ResponseEntity
            .noContent()
            .eTag(response.getETag().getText())
            .build();
    }


}

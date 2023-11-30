package thi.cnd.careerservice.http.jobapplication.query;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import thi.cnd.careerservice.api.generated.JobApplicationQueryApi;
import thi.cnd.careerservice.api.generated.model.JobApplicationResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobApplicationsResponseDTO;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationId;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;
import thi.cnd.careerservice.jobapplication.query.port.JobApplicationQueryService;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;
import thi.cnd.careerservice.user.model.UserId;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobApplicationQueryApiController implements JobApplicationQueryApi {

    private final JobApplicationQueryService jobOfferQueryService;
    private final JobApplicationQueryApiMapper mapper;

    @Override
    public ResponseEntity<JobApplicationsResponseDTO> getAllJobApplicationsByUserId(UUID userId) {
        var jobApplications = jobOfferQueryService.getAllJobApplicationsByUser(new UserId(userId));
        return ResponseEntity.ok(mapper.toDTO(jobApplications));
    }

    @Override
    public ResponseEntity<JobApplicationResponseDTO> getJobApplicationForCompany(
        UUID companyId, UUID jobOfferId, UUID jobApplicationId, Optional<String> ifNoneMatch
    ) {
        var eTag = ifNoneMatch.flatMap(ETag::fromIfNoneMatchHeader);
        var jobApplication = jobOfferQueryService.getJobApplication(new JobApplicationId(jobApplicationId), eTag);
        return buildResponseEntity(jobApplication);
    }

    @Override
    public ResponseEntity<JobApplicationResponseDTO> getJobApplicationForUser(
        UUID userId, UUID jobApplicationId, Optional<String> ifNoneMatch
    ) {
        var eTag = ifNoneMatch.flatMap(ETag::fromIfNoneMatchHeader);
        var jobApplication = jobOfferQueryService.getJobApplication(new JobApplicationId(jobApplicationId), eTag);
        return buildResponseEntity(jobApplication);
    }

    @Override
    public ResponseEntity<JobApplicationResponseDTO> getJobApplicationByUserAndJobOffer(UUID userId, UUID jobOfferId,
        Optional<String> ifNoneMatch) {
        var eTag = ifNoneMatch.flatMap(ETag::fromIfNoneMatchHeader);
        var jobApplication = jobOfferQueryService.getJobApplicationByUserAndJobOffer(new UserId(userId), new JobOfferId(jobOfferId), eTag);
        return buildResponseEntity(jobApplication);
    }

    @Override
    public ResponseEntity<JobApplicationsResponseDTO> getAllPublishedJobApplicationsByJobOffer(UUID companyId, UUID jobOfferId) {
        var jobApplications = jobOfferQueryService.getAllPublishedJobApplicationsByJobOffer(new JobOfferId(jobOfferId));
        return ResponseEntity.ok(mapper.toDTO(jobApplications));
    }

    private ResponseEntity<JobApplicationResponseDTO> buildResponseEntity(JobApplicationView jobApplication) {
        return ResponseEntity
            .ok()
            .eTag(ETag.weak(jobApplication.getMetadata().version()).getText())
            .body(mapper.toDTO(jobApplication));
    }

}

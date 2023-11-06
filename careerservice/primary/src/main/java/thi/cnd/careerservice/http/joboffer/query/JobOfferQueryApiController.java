package thi.cnd.careerservice.http.joboffer.query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import thi.cnd.careerservice.joboffer.view.model.SearchAvailableJobOfferFilter;
import thi.cnd.careerservice.shared.view.PaginationService;
import thi.cnd.careerservice.api.generated.JobOfferQueryApi;
import thi.cnd.careerservice.api.generated.model.JobOfferResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobOffersResponseDTO;
import thi.cnd.careerservice.api.generated.model.PaginatedJobOffersResponseDTO;
import thi.cnd.careerservice.shared.model.ETag;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.exception.ResourceNotFoundException;
import thi.cnd.careerservice.joboffer.model.JobOfferId;
import thi.cnd.careerservice.joboffer.view.query.JobOfferQueryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JobOfferQueryApiController implements JobOfferQueryApi {

    private final JobOfferQueryService jobOfferQueryService;
    private final JobOfferQueryApiMapper jobOfferQueryApiMapper;
    private final PaginationService paginationService;

    @Override
    public ResponseEntity<PaginatedJobOffersResponseDTO> searchAvailableJobOffers(Optional<Integer> skip,
        Optional<Integer> limit, Optional<String> title, Optional<List<UUID>> companyIds) {
        var searchFilter = new SearchAvailableJobOfferFilter(
            title,
            companyIds.orElse(List.of()).stream().map(CompanyId::new).toList(),
            paginationService.createPageableOfRequest(skip, limit)
        );
        var jobOffers = jobOfferQueryService.searchAvailableJobOffers(searchFilter);
        return ResponseEntity.ok(jobOfferQueryApiMapper.toDTO(jobOffers));
    }

    @Override
    public ResponseEntity<JobOfferResponseDTO> getAvailableJobOffer(UUID companyId, UUID jobOfferId, Optional<String> ifNoneMatch) {
        var eTag = ifNoneMatch.flatMap(ETag::fromIfNoneMatchHeader);
        var jobOffer = jobOfferQueryService.getAvailableJobOffer(new JobOfferId(jobOfferId), eTag);

        if (!Objects.equals(jobOffer.getCompanyId().getId(), companyId)) {
            throw new ResourceNotFoundException("Could not found job offer for this company.");
        }

        return ResponseEntity
            .ok()
            .eTag(ETag.weak(jobOffer.getMetadata().version()).getText())
            .body(jobOfferQueryApiMapper.toDTO(jobOffer));
    }

    @Override
    public ResponseEntity<JobOfferResponseDTO> getJobOffer(UUID companyId, UUID jobOfferId, Optional<String> ifNoneMatch) {
        var eTag = ifNoneMatch.flatMap(ETag::fromIfNoneMatchHeader);
        var jobOffer = jobOfferQueryService.getJobOffer(new JobOfferId(jobOfferId), eTag);

        if (!Objects.equals(jobOffer.getCompanyId().getId(), companyId)) {
            throw new ResourceNotFoundException("Could not found job offer for this company.");
        }

        return ResponseEntity
            .ok()
            .eTag(ETag.weak(jobOffer.getMetadata().version()).getText())
            .body(jobOfferQueryApiMapper.toDTO(jobOffer));
    }

    @Override
    public ResponseEntity<JobOffersResponseDTO>  getAvailableJobOffersByCompanyId(UUID companyId) {
        var jobOffers = jobOfferQueryService.getAvailableJobOffersByCompanyId(new CompanyId(companyId));
        return ResponseEntity.ok(jobOfferQueryApiMapper.toDTO(jobOffers));
    }

    @Override
    public ResponseEntity<JobOffersResponseDTO> getAllJobOffersByCompanyId(UUID companyId) {
        var jobOffers = jobOfferQueryService.getAllJobOffersByCompanyId(new CompanyId(companyId));
        return ResponseEntity.ok(jobOfferQueryApiMapper.toDTO(jobOffers));
    }


}

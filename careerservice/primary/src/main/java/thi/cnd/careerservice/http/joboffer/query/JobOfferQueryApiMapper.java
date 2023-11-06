package thi.cnd.careerservice.http.joboffer.query;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import thi.cnd.careerservice.api.generated.model.JobOfferResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobOffersResponseDTO;
import thi.cnd.careerservice.api.generated.model.PaginatedJobOffersResponseDTO;
import thi.cnd.careerservice.joboffer.view.model.JobOfferView;
import thi.cnd.careerservice.http.SharedMapper;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING, uses = SharedMapper.class,
    unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface JobOfferQueryApiMapper {

    default JobOffersResponseDTO toDTO(List<JobOfferView> jobOfferView) {
        return new JobOffersResponseDTO(jobOfferView.stream().map(this::toDTO).toList());
    }

    default PaginatedJobOffersResponseDTO toDTO(Page<JobOfferView> jobOfferView) {
        return new PaginatedJobOffersResponseDTO(
            SharedMapper.toPaginationDTO(jobOfferView),
            jobOfferView.getContent().stream().map(this::toDTO).toList()
        );
    }

    @Mapping(target = "version", source = "metadata.version")
    JobOfferResponseDTO toDTO(JobOfferView jobOfferView);
}

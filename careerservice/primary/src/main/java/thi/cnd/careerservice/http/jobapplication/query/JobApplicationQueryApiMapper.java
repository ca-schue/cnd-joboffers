package thi.cnd.careerservice.http.jobapplication.query;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import thi.cnd.careerservice.api.generated.model.JobApplicationResponseDTO;
import thi.cnd.careerservice.api.generated.model.JobApplicationsResponseDTO;
import thi.cnd.careerservice.http.CommonDTOMapper;
import thi.cnd.careerservice.jobapplication.query.domain.model.JobApplicationView;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING, uses = CommonDTOMapper.class,
    unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface JobApplicationQueryApiMapper {

    default JobApplicationsResponseDTO toDTO(List<JobApplicationView> jobApplications) {
        return new JobApplicationsResponseDTO(jobApplications.stream().map(this::toDTO).toList());
    }

    @Mapping(target = "version", source = "metadata.version")
    JobApplicationResponseDTO toDTO(JobApplicationView view);

}

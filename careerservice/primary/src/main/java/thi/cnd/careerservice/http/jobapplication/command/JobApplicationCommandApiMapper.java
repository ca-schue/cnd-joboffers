package thi.cnd.careerservice.http.jobapplication.command;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import thi.cnd.careerservice.api.generated.model.JobApplicationCreationRequestDTO;
import thi.cnd.careerservice.exception.InvalidInputReceivedException;
import thi.cnd.careerservice.http.CommonDTOMapper;
import thi.cnd.careerservice.jobapplication.command.application.model.JobApplicationCommand.CreateJobApplication;
import thi.cnd.careerservice.jobapplication.command.domain.model.JobApplicationStatus;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.joboffer.command.domain.model.JobOfferId;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING, uses = CommonDTOMapper.class,
    unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface JobApplicationCommandApiMapper {

    CreateJobApplication toCommand(UserId userId, JobOfferId jobOfferId, JobApplicationCreationRequestDTO requestDTO);

    default JobApplicationStatus toStatus(String status) {
        return JobApplicationStatus.parse(status).orElseThrow(() ->
            new InvalidInputReceivedException("Could not parse job application status")
        );
    }

}

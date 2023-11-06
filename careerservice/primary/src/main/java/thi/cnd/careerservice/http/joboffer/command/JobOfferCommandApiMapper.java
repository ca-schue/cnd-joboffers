package thi.cnd.careerservice.http.joboffer.command;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import thi.cnd.careerservice.api.generated.model.JobOfferCreationRequestDTO;
import thi.cnd.careerservice.api.generated.model.JobOfferUpdateRequestDTO;
import thi.cnd.careerservice.http.SharedMapper;
import thi.cnd.careerservice.joboffer.command.JobOfferCommand.CreateJobOffer;
import thi.cnd.careerservice.joboffer.command.JobOfferCommand.UpdateJobOfferAttributes;
import thi.cnd.careerservice.joboffer.model.JobOfferId;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING, uses = SharedMapper.class,
    unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface JobOfferCommandApiMapper {

    CreateJobOffer toCommand(JobOfferCreationRequestDTO dto);

    UpdateJobOfferAttributes toCommand(JobOfferId jobOfferId, JobOfferUpdateRequestDTO dto);

}

package thi.cnd.authservice.primary.rest.model;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.api.generated.model.ClientDTO;
import thi.cnd.authservice.core.model.Client;
import thi.cnd.authservice.core.model.ClientWithPlaintextPassword;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientApiMapper {

    ClientApiMapper INSTANCE = Mappers.getMapper(ClientApiMapper.class);

    default ClientCreationResponseDTO toDTO(ClientWithPlaintextPassword clientAndPassword) {
        return new ClientCreationResponseDTO()
                .name(clientAndPassword.client().name())
                .audiences(clientAndPassword.client().audiences())
                .password(clientAndPassword.password());
    }

    default ClientDTO toDTO(Client client) {
        return new ClientDTO()
                .name(client.name())
                .audiences(client.audiences())
                .lastPasswordChange(client.lastPasswordChange());
    }

}
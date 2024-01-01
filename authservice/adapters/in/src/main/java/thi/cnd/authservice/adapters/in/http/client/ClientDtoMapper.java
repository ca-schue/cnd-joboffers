package thi.cnd.authservice.adapters.in.http.client;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.api.generated.model.ClientDTO;
import thi.cnd.authservice.domain.model.client.Client;
import thi.cnd.authservice.domain.model.client.ClientWithPlaintextPassword;

import java.util.Collections;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ClientDtoMapper {

    ClientDtoMapper INSTANCE = Mappers.getMapper(ClientDtoMapper.class);

    default ClientCreationResponseDTO toDTO(ClientWithPlaintextPassword clientAndPassword) {
        return new ClientCreationResponseDTO()
                .name(clientAndPassword.client().name())
                .audiences(clientAndPassword.client().audiences())
                .password(clientAndPassword.password())
                .scopes(clientAndPassword.client().scopes() == null ? Collections.emptySet() : clientAndPassword.client().scopes());
    }

    default ClientDTO toDTO(Client client) {
        return new ClientDTO()
                .name(client.name())
                .audiences(client.audiences())
                .scopes(client.scopes() == null ? Collections.emptySet() : client.scopes())
                .lastPasswordChange(client.lastPasswordChange());
    }

}
package thi.cnd.authservice.primary.rest.client;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.api.generated.model.ClientDTO;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.model.client.ClientAccessToken;
import thi.cnd.authservice.core.model.client.ClientWithPlaintextPassword;

import java.util.Collections;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientApiMapper {

    ClientApiMapper INSTANCE = Mappers.getMapper(ClientApiMapper.class);

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
    default Jwt toJwt(ClientAccessToken clientAccessToken) {
        return clientAccessToken.signedClientJwt();
    }
}
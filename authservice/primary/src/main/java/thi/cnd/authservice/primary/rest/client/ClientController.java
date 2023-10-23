package thi.cnd.authservice.primary.rest.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.api.generated.ClientManagementApi;
import thi.cnd.authservice.api.generated.model.ClientCreationRequestDTO;
import thi.cnd.authservice.api.generated.model.ClientCreationResponseDTO;
import thi.cnd.authservice.core.domain.ClientService;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.model.client.ClientAccessToken;
import thi.cnd.authservice.core.model.client.ClientWithPlaintextPassword;
import thi.cnd.authservice.core.ports.primary.ClientServicePort;
import thi.cnd.authservice.primary.security.authentication.loginAuthentication.oauth2Client.CustomRegisteredClient;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ClientController implements ClientManagementApi {

    private final ClientServicePort service;
    private final ClientApiMapper mapper;

    public OAuth2TokenGenerator<Jwt> loginClient() {
        return (context) -> {
            OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = context.getPrincipal();
            CustomRegisteredClient customRegisteredClient = (CustomRegisteredClient) oAuth2ClientAuthenticationToken.getRegisteredClient();
            Client client = customRegisteredClient.getClient();
            ClientAccessToken clientAccessToken = service.mintClientAccessToken(client);
            return mapper.toJwt(clientAccessToken);
        };
    }

    @Override
    public ResponseEntity<ClientCreationResponseDTO> createNewClient(ClientCreationRequestDTO requestDTO) {
        Set<String> audiences = requestDTO.getAudiences() != null ? requestDTO.getAudiences() : Set.of();
        try {
           ClientWithPlaintextPassword clientWithPlaintextPassword = service.createNewClient(requestDTO.getName(), audiences, requestDTO.getScopes());
            return ResponseEntity.ok(mapper.toDTO(clientWithPlaintextPassword));
        } catch (ClientAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> deleteClient(String name) {
        try {
            service.deleteClient(name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ClientNotFoundByNameException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ClientCreationResponseDTO> resetPassword(String clientId) {
        try {
            ClientWithPlaintextPassword clientWithNewPlaintextPassword = service.setNewRandomPassword(clientId);
            return ResponseEntity.ok(mapper.toDTO(clientWithNewPlaintextPassword));
        } catch (ClientNotFoundByNameException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

}

package thi.cnd.authservice.primary.security.authentication.loginAuthentication.oauth2Client;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.ports.secondary.ClientRepositoryPort;


// This class is required by the OAuth2 "ClientSecretAuthenticationProvider" to authorize an incoming client credentials grant
// It queries a registered client and verifies the credentials
// If successful, the SecurityContextHolder is populated with the RegisteredClient as the principal inside an OAuth2ClientCredentialsAuthenticationToken
// This Token is converted in the JWT which is sent to the client
@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientRepositoryPort port;

    @Override
    public void save(RegisteredClient registeredClient) {
        // We do not want to save the registered clients. This would change their passwords and break the login of services.
        // TODO: nevertheless change last login => save() only method which is called after successful login
    }

    @Override
    public RegisteredClient findById(String id) {
        try {
            return findClientByNameAndUpdateLastLogin(id);
        } catch (ClientNotFoundByNameException e) {
            throw new RuntimeException(e); // Must be runtime! (Handled by OAuth2 lib)
        }
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        try {
            return findClientByNameAndUpdateLastLogin(clientId); // TODO: change to login-attempt
        } catch (ClientNotFoundByNameException e) {
            throw new RuntimeException(e); // Must be runtime! (Handled by OAuth2 lib)
        }
    }
    private RegisteredClient findClientByNameAndUpdateLastLogin(String clientId) throws ClientNotFoundByNameException {
        Client client = port.findByNameAndUpdateLastLogin(clientId);
        RegisteredClient regClient =  RegisteredClient
                .withId(client.name())
                .clientId(client.name())
                .clientSecret(client.encryptedPassword())
                .scopes(scopes -> scopes.addAll(client.scopes()))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();
        return regClient;
    }

}

package thi.cnd.authservice.adapters.in.security.oauth2ClientLogin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.domain.ClientService;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.Client;


// This class is required by the OAuth2 "ClientSecretAuthenticationProvider" to authorize an incoming client credentials grant
// It queries a registered client and verifies the credentials
// If successful, the SecurityContextHolder is populated with the RegisteredClient as the principal inside an OAuth2ClientCredentialsAuthenticationToken
// This Token is converted in the JWT which is sent to the client
@Component
@RequiredArgsConstructor
class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;

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
            return null;
            //throw new RuntimeException(e); // Must be runtime! (Handled by OAuth2 lib)
        }
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        try {
            return findClientByNameAndUpdateLastLogin(clientId); // TODO: change to login-attempt
        } catch (ClientNotFoundByNameException e) {
            return null;
            // throw new RuntimeException(e); // Must be runtime! (Handled by OAuth2 lib)
        }
    }
    private RegisteredClient findClientByNameAndUpdateLastLogin(String clientId) throws ClientNotFoundByNameException {
        Client client = clientService.updateLastLogin(clientId);
        return new CustomRegisteredClient(client);
    }

}

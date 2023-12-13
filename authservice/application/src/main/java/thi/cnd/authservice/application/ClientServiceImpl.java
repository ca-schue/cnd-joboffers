package thi.cnd.authservice.application;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.application.ports.out.repository.ClientRepository;
import thi.cnd.authservice.application.ports.out.security.PasswordProvider;
import thi.cnd.authservice.application.ports.out.security.TokenProvider;
import thi.cnd.authservice.domain.ClientService;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.client.*;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
class ClientServiceImpl implements ClientService {

    private final ClientRepository port;
    private final PasswordProvider passwordProvider;
    private final TokenProvider tokenProvider;

    @Override
    public ClientWithPlaintextPassword createNewClient(String name, Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException {
        var password = this.passwordProvider.generatePassword();
        var encryptedPassword = this.passwordProvider.encodePassword(password);
        var now = Instant.now();

        Client client = new Client(name, encryptedPassword, audiences, scopes, now, now);
        return new ClientWithPlaintextPassword(port.register(client), password);
    }

    @Override
    public AccessToken mintClientAccessToken(Client client) {
        return tokenProvider.createClientAccessToken(client);
    }

    @Override
    public ClientWithPlaintextPassword setNewRandomPassword(@NotBlank String name) throws ClientNotFoundByNameException {
        var password = this.passwordProvider.generatePassword();
        var encryptedPassword = this.passwordProvider.encodePassword(password);
        var savedClient = port.updatePassword(name, encryptedPassword);
        return new ClientWithPlaintextPassword(savedClient, password);
    }

    @Override
    public void deleteClient(String name) throws ClientNotFoundByNameException {
        port.delete(name);
    }

    @Override
    public Client updateLastLogin(String name) throws ClientNotFoundByNameException {
        return port.findByNameAndUpdateLastLogin(name);
    }

}

package thi.cnd.authservice.core.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.core.domain.password.PasswordEncoder;
import thi.cnd.authservice.core.domain.password.PasswordGenerator;
import thi.cnd.authservice.core.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.model.client.ClientWithPlaintextPassword;
import thi.cnd.authservice.core.ports.primary.ClientServicePort;
import thi.cnd.authservice.core.ports.secondary.ClientRepositoryPort;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientRepositoryPort port;
    private final PasswordEncoder encoder;
    private final PasswordGenerator passwordGenerator;

    @Override
    public ClientWithPlaintextPassword createNewClient(String name, Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException {
        var password = passwordGenerator.generatePassword();
        var encryptedPassword = encoder.encode(password);
        var now = Instant.now();

        Client client = new Client(name, encryptedPassword, audiences, scopes, now, now);
        return new ClientWithPlaintextPassword(port.register(client), password);
    }

    @Override
    public ClientWithPlaintextPassword setNewRandomPassword(@NotBlank String name) throws ClientNotFoundByNameException {
        var password = passwordGenerator.generatePassword();
        var encryptedPassword = encoder.encode(password);
        var savedClient = port.updatePassword(name, encryptedPassword);
        return new ClientWithPlaintextPassword(savedClient, password);
    }

    @Override
    public void deleteClient(String name) throws ClientNotFoundByNameException {
        port.delete(name);
    }

}

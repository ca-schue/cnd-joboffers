package thi.cnd.authservice.application;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.authservice.domain.ClientServicePort;
import thi.cnd.authservice.domain.jwt.JwtProvider;
import thi.cnd.authservice.domain.password.PasswordEncoder;
import thi.cnd.authservice.domain.password.PasswordGenerator;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.client.*;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.application.ports.out.repository.ClientRepositoryPort;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientServicePort {

    private final ClientRepositoryPort port;
    private final PasswordEncoder encoder;
    private final PasswordGenerator passwordGenerator;
    private final JwtProvider jwtProvider;

    @Override
    public ClientWithPlaintextPassword createNewClient(String name, Set<String> audiences, Set<String> scopes) throws ClientAlreadyExistsException {
        var password = passwordGenerator.generatePassword();
        var encryptedPassword = encoder.encode(password);
        var now = Instant.now();

        Client client = new Client(name, encryptedPassword, audiences, scopes, now, now);
        return new ClientWithPlaintextPassword(port.register(client), password);
    }

    @Override
    public ClientAccessToken mintClientAccessToken(Client client) {
        return jwtProvider.createClientAccessToken(client);
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

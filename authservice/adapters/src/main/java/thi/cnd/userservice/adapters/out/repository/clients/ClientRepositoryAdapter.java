package thi.cnd.userservice.adapters.out.repository.clients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.application.ports.out.repository.ClientRepositoryPort;
import thi.cnd.authservice.domain.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.Client;
import thi.cnd.userservice.adapters.out.repository.clients.model.ClientDaoMapper;

import java.time.Instant;

@Validated
@Component
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientMongoDBRepository repository;
    private final ClientDaoMapper mapper;

    @Override
    public Client findByName(@NotBlank String name) throws ClientNotFoundByNameException {
        return repository.findByName(name)
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Client" + name + " not found."));
    }

    @Override
    public Client findByNameAndUpdateLastLogin(@NotBlank String name) throws ClientNotFoundByNameException {
        return repository.findByNameAndUpdateLastLogin(name, Instant.now())
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Client" + name + " not found."));
    }

    @Override
    public Client register(@NotNull Client client) throws ClientAlreadyExistsException {
        try {
            var dao = mapper.toDAO(client);
            var savedClient = repository.insert(dao);
            return mapper.toClient(savedClient);
        } catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client" + client.name() + " already registered.");
        }
    }

    // TODO: why needed?
    @Override
    public Client save(@NotNull Client client) throws ClientAlreadyExistsException {
        try {
            var dao = mapper.toDAO(client);
            var savedClient = repository.save(dao);
            return mapper.toClient(savedClient);
        } catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client" + client.name() + " already registered.");
        }
    }

    @Override
    public Client updatePassword(@NotBlank String name, @NotBlank String encryptedPassword) throws ClientNotFoundByNameException {
        return repository.updatePassword(name, encryptedPassword)
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Could not find client with name " + name));
    }

    @Override
    public void delete(String name) throws ClientNotFoundByNameException {
        repository.delete(name);
    }
}

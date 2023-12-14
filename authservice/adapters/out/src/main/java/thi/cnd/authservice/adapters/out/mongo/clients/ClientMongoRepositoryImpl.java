package thi.cnd.authservice.adapters.out.mongo.clients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.adapters.out.mongo.clients.DAOs.ClientDAO;
import thi.cnd.authservice.adapters.out.mongo.clients.DAOs.ClientDaoMapper;
import thi.cnd.authservice.application.ports.out.repository.ClientRepository;
import thi.cnd.authservice.domain.exceptions.ClientAlreadyExistsException;
import thi.cnd.authservice.domain.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.domain.model.client.Client;

import java.time.Instant;
import java.util.Optional;

@Validated
@Component
@RequiredArgsConstructor
class ClientMongoRepositoryImpl implements ClientRepository {

    private final MongoTemplate mongoTemplate;
    private final ClientDaoMapper mapper;

    @Override
    public Client findByName(@NotBlank String name) throws ClientNotFoundByNameException {
        var query = queryByName(name);
        return Optional.ofNullable(mongoTemplate.findOne(query, ClientDAO.class))
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Client" + name + " not found."));
    }

    @Override
    public Client findByNameAndUpdateLastLogin(@NotBlank String name) throws ClientNotFoundByNameException {
        var query = queryByName(name);
        var update = Update.update("lastLogin", Instant.now());
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, ClientDAO.class))
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Client" + name + " not found."));
    }

    @Override
    public Client register(@NotNull Client client) throws ClientAlreadyExistsException {
        try {
            var dao = mapper.toDAO(client);
            var savedClient = mongoTemplate.insert(dao);
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
            var savedClient = mongoTemplate.save(dao);
            return mapper.toClient(savedClient);
        } catch (DuplicateKeyException e) {
            throw new ClientAlreadyExistsException("Client" + client.name() + " already registered.");
        }
    }

    @Override
    public Client updatePassword(@NotBlank String name, @NotBlank String encryptedPassword) throws ClientNotFoundByNameException {
        var now = Instant.now();
        var query = queryByName(name);
        var update = new Update()
                .set("encryptedPassword", encryptedPassword)
                .set("lastLogin", now)
                .set("lastPasswordChange", now);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, ClientDAO.class))
                .map(mapper::toClient)
                .orElseThrow(() -> new ClientNotFoundByNameException("Could not find client with name " + name));
    }

    @Override
    public void delete(String name) throws ClientNotFoundByNameException {
        var query = queryByName(name);
        if(! mongoTemplate.remove(query, ClientDAO.class).wasAcknowledged()) {
            throw new ClientNotFoundByNameException("Client with name " + name + " could not be deleted");
        }
    }

    private Query queryByName(String name) {
        return new Query(Criteria.where("name").is(name));
    }

}

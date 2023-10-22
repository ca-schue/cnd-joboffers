package thi.cnd.authservice.secondary.repository.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.secondary.repository.clients.model.ClientDAO;

import java.time.Instant;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientMongoDBRepository {

    private final MongoTemplate mongoTemplate;

    public Optional<ClientDAO> findByNameAndUpdateLastLogin(String name, Instant lastLogin) {
        var query = queryByName(name);
        var update = Update.update("lastLogin", lastLogin);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, ClientDAO.class));
    }

    public Optional<ClientDAO> updatePassword(String name, String encryptedPassword) {
        var now = Instant.now();

        var query = queryByName(name);
        var update = new Update()
                .set("encryptedPassword", encryptedPassword)
                .set("lastLogin", now)
                .set("lastPasswordChange", now);

        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, ClientDAO.class));
    }


    public Optional<ClientDAO> findByName(String name) {
        var query = queryByName(name);
        return Optional.ofNullable(mongoTemplate.findOne(query, ClientDAO.class));
    }


    public void delete(String name) throws ClientNotFoundByNameException {
        var query = queryByName(name);
        if(! mongoTemplate.remove(query, ClientDAO.class).wasAcknowledged()) {
            throw new ClientNotFoundByNameException("Client with name " + name + " could not be deleted");
        }
    }

    public ClientDAO save(ClientDAO client) {
        return mongoTemplate.save(client);
    }

    public ClientDAO insert(ClientDAO client) {
        return mongoTemplate.insert(client);
    }

    private Query queryByName(String name) {
        return new Query(Criteria.where("name").is(name));
    }

}
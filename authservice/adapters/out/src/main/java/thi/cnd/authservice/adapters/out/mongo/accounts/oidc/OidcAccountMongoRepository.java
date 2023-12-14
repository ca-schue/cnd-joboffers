package thi.cnd.authservice.adapters.out.mongo.accounts.oidc;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.OidcAccountDAO;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.util.Optional;

public interface OidcAccountMongoRepository extends MongoRepository<OidcAccountDAO, AccountId>, ComplexOidcAccountMongoRepository {

    Optional<OidcAccountDAO> findBySubject(String subject);

}

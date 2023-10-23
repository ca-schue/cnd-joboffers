package thi.cnd.authservice.secondary.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.secondary.repository.account.model.InternalAccountDAO;
import thi.cnd.authservice.secondary.repository.account.model.OidcAccountDAO;

import java.util.Optional;

public interface OidcAccountMongoDBRepository extends MongoRepository<OidcAccountDAO, AccountId>, OidcAccountMongoDBRepositoryComplex {

    Optional<OidcAccountDAO> findBySubject(String subject);

}

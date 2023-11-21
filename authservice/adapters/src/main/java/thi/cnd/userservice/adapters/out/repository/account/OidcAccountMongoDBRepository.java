package thi.cnd.userservice.adapters.out.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.account.model.OidcAccountDAO;

import java.util.Optional;

public interface OidcAccountMongoDBRepository extends MongoRepository<OidcAccountDAO, AccountId>, OidcAccountMongoDBRepositoryComplex {

    Optional<OidcAccountDAO> findBySubject(String subject);

}

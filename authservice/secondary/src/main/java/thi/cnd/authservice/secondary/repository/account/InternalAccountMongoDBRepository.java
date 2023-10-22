package thi.cnd.authservice.secondary.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.secondary.repository.account.model.InternalAccountDAO;

import java.util.Optional;

public interface InternalAccountMongoDBRepository extends MongoRepository<InternalAccountDAO, AccountId>, InternalAccountMongoDBRepositoryComplex {

    Optional<InternalAccountDAO> findByEmail(String email);
}

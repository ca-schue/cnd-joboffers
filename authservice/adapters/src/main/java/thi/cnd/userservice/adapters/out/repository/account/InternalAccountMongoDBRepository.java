package thi.cnd.userservice.adapters.out.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.account.model.InternalAccountDAO;

import java.util.Optional;

public interface InternalAccountMongoDBRepository extends MongoRepository<InternalAccountDAO, AccountId>, InternalAccountMongoDBRepositoryComplex {

    Optional<InternalAccountDAO> findByEmail(String email);
}

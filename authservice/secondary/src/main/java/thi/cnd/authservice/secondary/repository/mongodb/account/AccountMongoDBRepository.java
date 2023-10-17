package thi.cnd.authservice.secondary.repository.mongodb.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.secondary.repository.mongodb.account.model.AccountDAO;

import java.util.Optional;

// Reason behind double interfaces, see: https://stackoverflow.com/a/50186472
public interface AccountMongoDBRepository extends MongoRepository<AccountDAO, AccountId>, AccountMongoDBRepositoryComplex {

    Optional<AccountDAO> deleteOneById(AccountId id);

    Optional<AccountDAO> findByEmail(String email);
}

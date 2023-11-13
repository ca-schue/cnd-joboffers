package thi.cnd.authservice.secondary.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.secondary.repository.account.model.AccountDAO;

import java.util.Optional;

// Reason behind double interfaces, see: https://stackoverflow.com/a/50186472
public interface AccountMongoDBRepository extends MongoRepository<? extends AccountDAO, AccountId> {
    Optional<AccountDAO> deleteOneById(AccountId id);
}

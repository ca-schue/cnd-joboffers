package thi.cnd.authservice.adapters.out.mongo.accounts;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.AccountDAO;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.util.Optional;

// Reason behind double interfaces, see: https://stackoverflow.com/a/50186472
public interface AccountMongoRepository extends MongoRepository<AccountDAO, AccountId> {
    Optional<AccountDAO> deleteOneById(AccountId id);
}

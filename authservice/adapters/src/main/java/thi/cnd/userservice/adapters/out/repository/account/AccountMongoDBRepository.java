package thi.cnd.userservice.adapters.out.repository.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.account.model.AccountDAO;

import java.util.Optional;

// Reason behind double interfaces, see: https://stackoverflow.com/a/50186472
public interface AccountMongoDBRepository extends MongoRepository<AccountDAO, AccountId> {
    Optional<AccountDAO> deleteOneById(AccountId id);
}

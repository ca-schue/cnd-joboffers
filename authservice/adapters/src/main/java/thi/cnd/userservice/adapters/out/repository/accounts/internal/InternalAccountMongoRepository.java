package thi.cnd.userservice.adapters.out.repository.accounts.internal;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.accounts.model.InternalAccountDAO;

import java.util.Optional;

public interface InternalAccountMongoRepository extends MongoRepository<InternalAccountDAO, AccountId>, ComplexInternalAccountMongoRepository {

    Optional<InternalAccountDAO> findByEmail(String email);
}

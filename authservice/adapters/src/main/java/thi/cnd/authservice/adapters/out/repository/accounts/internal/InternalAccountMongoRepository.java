package thi.cnd.authservice.adapters.out.repository.accounts.internal;

import org.springframework.data.mongodb.repository.MongoRepository;
import thi.cnd.authservice.adapters.out.repository.accounts.DAOs.InternalAccountDAO;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.util.Optional;

public interface InternalAccountMongoRepository extends MongoRepository<InternalAccountDAO, AccountId>, ComplexInternalAccountMongoRepository {


    Optional<InternalAccountDAO> findByEmail(String email);
}

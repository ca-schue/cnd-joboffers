package thi.cnd.authservice.secondary.repository.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.secondary.repository.account.model.InternalAccountDAO;

import java.util.Optional;

public interface InternalAccountMongoDBRepositoryComplex {

    public Optional<InternalAccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email);

    public Optional<InternalAccountDAO> findByIdAndUpdateEmail(@NotNull AccountId accountId, @Email String newEmail);
}

package thi.cnd.authservice.adapters.out.mongo.accounts.internal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.InternalAccountDAO;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.util.Optional;

public interface ComplexInternalAccountMongoRepository {

    public Optional<InternalAccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email);

    public Optional<InternalAccountDAO> findByIdAndUpdateEmail(@NotNull AccountId accountId, @Email String newEmail);
}
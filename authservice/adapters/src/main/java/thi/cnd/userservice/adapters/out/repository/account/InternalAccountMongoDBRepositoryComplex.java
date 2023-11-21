package thi.cnd.userservice.adapters.out.repository.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.account.model.InternalAccountDAO;

import java.util.Optional;

public interface InternalAccountMongoDBRepositoryComplex {

    public Optional<InternalAccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email);

    public Optional<InternalAccountDAO> findByIdAndUpdateEmail(@NotNull AccountId accountId, @Email String newEmail);
}

package thi.cnd.authservice.core.ports.secondary;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountId;

import java.util.Optional;

@Validated
public interface AccountRepositoryPort {

    @NotNull Account save(@NotNull Account account) throws AccountAlreadyExistsException;

    Account findAccountByEmailAndUpdateLastLogin(@NotBlank String email) throws AccountNotFoundByEmailException;


    Account findAccountById(@NotNull AccountId accountId) throws AccountNotFoundByIdException;

    void delete(@NotNull AccountId id) throws AccountNotFoundByIdException;

    Account findAccountByEmail(String email) throws AccountNotFoundByEmailException;

    //Account updateEmail(AccountId accountId, String email);

}

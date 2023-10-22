package thi.cnd.authservice.core.ports.secondary;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;

@Validated
public interface AccountRepositoryPort {

    InternalAccount saveInternalAccount(InternalAccount internalAccount) throws AccountAlreadyExistsException;

    public OidcAccount saveOidcAccount(OidcAccount oidcAccount) throws AccountAlreadyExistsException;

    Account findAccountById(@NotNull AccountId accountId) throws AccountNotFoundByIdException;

    void delete(@NotNull AccountId id) throws AccountNotFoundByIdException;

    InternalAccount findInternalAccountByEmailAndUpdateLastLogin(@NotBlank String email) throws AccountNotFoundByEmailException;

    InternalAccount findInternalAccountByEmail(String email) throws AccountNotFoundByEmailException;

    InternalAccount updateInternalAccountEmail(AccountId userId, String email) throws AccountNotFoundByIdException;

}

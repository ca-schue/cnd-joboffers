package thi.cnd.authservice.core.ports.secondary;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;

@Validated
public interface AccountRepositoryPort {


    Account saveAccount(Account account) throws AccountAlreadyExistsException;
    InternalAccount saveInternalAccount(InternalAccount internalAccount) throws AccountAlreadyExistsException;
    OidcAccount saveOidcAccount(OidcAccount oidcAccount) throws AccountAlreadyExistsException;

    Account findAccountById(@NotNull AccountId accountId) throws AccountNotFoundByIdException;
    InternalAccount findInternalAccountByEmailAndUpdateLastLogin(@NotBlank String email) throws AccountNotFoundByEmailException;
    InternalAccount findInternalAccountByEmail(String email) throws AccountNotFoundByEmailException;
    InternalAccount findInternalAccountById(@NotNull AccountId accountId) throws AccountNotFoundByIdException, WrongProviderException;
    OidcAccount findOidcAccountById(@NotNull AccountId accountId) throws AccountNotFoundByIdException, WrongProviderException;
    OidcAccount findOidcAccountBySubjectAndUpdateLastLogin(@NotBlank String subject) throws AccountNotFoundBySubjectException;

    Account updateAccount(Account updatedAccount) throws AccountNotFoundByIdException, WrongProviderException;
    InternalAccount updateInternalAccount(InternalAccount updatedInternalAccount) throws AccountNotFoundByIdException, WrongProviderException;
    OidcAccount updateOidcAccount(OidcAccount updatedOidcAccount) throws AccountNotFoundByIdException, WrongProviderException;


    void delete(@NotNull AccountId id) throws AccountNotFoundByIdException;

}

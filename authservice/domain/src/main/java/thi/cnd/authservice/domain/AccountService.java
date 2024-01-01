package thi.cnd.authservice.domain;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.*;

@Validated
public interface AccountService {

    public AccessToken mintAccountAccessToken(Account account);

    void validateAccount(AccountId accountId);
    void invalidateAccount(AccountId accountId);

    InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException;
    OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException;

    void updateInternalAccountPassword(AccountId accountId, String newPlaintextPassword) throws AccountNotFoundByIdException, InvalidPasswordException, WrongProviderException;
    InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException, WrongProviderException;

    InternalAccount updateLastInternalAccountLogin(String email) throws AccountNotFoundByEmailException;
    OidcAccount updateLastOidcAccountLogin(String subject) throws AccountNotFoundBySubjectException;

    void deleteAccount(@NotNull AccountId accountId) throws AccountNotFoundByIdException, AccountStillVerifiedException;


}

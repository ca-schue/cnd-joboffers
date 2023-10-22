package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;

@Validated
public interface AccountServicePort {



    public AccountAccessToken mintAccessToken(Account account);
    void deleteAccount (@NotNull AccountId accountId) throws AccountNotFoundByIdException;

    InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException;
    OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException;

    InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException;

}

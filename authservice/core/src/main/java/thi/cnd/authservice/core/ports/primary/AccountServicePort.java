package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;

@Validated
public interface AccountServicePort {

    public AccountAccessToken mintAccountAccessToken(Account account);

    InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException;
    OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException;

    void updateInternalAccountPassword(AccountId accountId, String newPlaintextPassword) throws AccountNotFoundByIdException, InvalidPasswordException, WrongProviderException;
    InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException, WrongProviderException;

    void deleteAccount(@NotNull AccountId accountId) throws AccountNotFoundByIdException;


}

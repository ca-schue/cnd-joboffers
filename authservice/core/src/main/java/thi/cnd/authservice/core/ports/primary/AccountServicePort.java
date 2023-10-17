package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.AccountProvider;

@Validated
public interface AccountServicePort {

    AccountAccessToken mintAccessTokenInternalProvider(String email) throws AccountNotFoundByEmailException, WrongProviderException;

    AccountAccessToken mintAccessTokenOidcProvider(String email) throws WrongProviderException;

    void deleteAccount (@NotNull AccountId accountId) throws AccountNotFoundByIdException;

    Account registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException;

    // Account updateAccountEmail(@NotNull AccountId accountId, @Email String email);

}

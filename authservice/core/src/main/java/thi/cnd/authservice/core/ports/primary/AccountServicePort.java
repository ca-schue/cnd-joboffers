package thi.cnd.authservice.core.ports.primary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.exceptions.WrongProviderException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.AccountProvider;

public interface AccountServicePort {

    AccountAccessToken mintAccessTokenInternalProvider(String email) throws AccountNotFoundByEmailException, WrongProviderException;

    AccountAccessToken mintAccessTokenOidcProvider(String email) throws WrongProviderException;

    void deleteAccount (@NotNull AccountId accountId) throws AccountNotFoundByIdException;

    Account registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException;

    // Account updateAccountEmail(@NotNull AccountId accountId, @Email String email);

}

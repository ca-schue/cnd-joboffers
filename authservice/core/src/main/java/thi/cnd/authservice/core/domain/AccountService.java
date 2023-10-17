package thi.cnd.authservice.core.domain;

import lombok.AllArgsConstructor;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.exceptions.WrongProviderException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.model.AccountProvider;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

@AllArgsConstructor
public class AccountService implements AccountServicePort {

    private final AccountJwtProvider accountJwtProvider;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountFactory accountFactory;

    @Override
    public Account registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException {
        Account account = accountFactory.buildInternal(email, password);
        return accountRepositoryPort.save(account);
    }

    private Account registerNewOidcAccount(String email) throws AccountAlreadyExistsException {
        Account account = accountFactory.buildOidc(email);
        return accountRepositoryPort.save(account);
    }

    @Override
    public AccountAccessToken mintAccessTokenInternalProvider(String email) throws AccountNotFoundByEmailException, WrongProviderException {
        Account account = getValidatedAccount(AccountProvider.INTERNAL, email);
        return accountJwtProvider.createJwt(account);
    }

    @Override
    public AccountAccessToken mintAccessTokenOidcProvider(String email) throws WrongProviderException {
        Account account;
        try {
            account = getValidatedAccount(AccountProvider.OIDC, email);
        } catch (AccountNotFoundByEmailException e) {
            try {
                account = this.registerNewOidcAccount(email);
            } catch (AccountAlreadyExistsException ex) {
                throw new RuntimeException("Race condition error. Please try again.");
            }
        }
        return accountJwtProvider.createJwt(account);
    }

    private Account getValidatedAccount(AccountProvider accountProvider, String email) throws AccountNotFoundByEmailException, WrongProviderException {
        Account account = accountRepositoryPort.findAccountByEmail(email);
        if (account.provider() != accountProvider) {
            throw new WrongProviderException("Account linked to email " + email + "requires an access token provided by an " + accountProvider.name() + " itentiy provider");
        } else {
            return account;
        }
    }

    @Override
    public void deleteAccount(AccountId accountId) throws AccountNotFoundByIdException {
        accountRepositoryPort.delete(accountId);
    }

}

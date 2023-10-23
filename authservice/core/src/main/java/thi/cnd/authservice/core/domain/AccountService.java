package thi.cnd.authservice.core.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.domain.jwt.JwtProvider;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;

@Service
@Validated
@AllArgsConstructor
public class AccountService implements AccountServicePort {

    private final JwtProvider jwtProvider;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountFactory accountFactory;

    @Override
    public InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException {
        InternalAccount internalAccount = accountFactory.buildInternal(email, password);
        accountRepositoryPort.saveInternalAccount(internalAccount);
        return internalAccount;
    }

    @Override
    public OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException {
        OidcAccount oidcAccount = accountFactory.buildOidc(subject);
        accountRepositoryPort.saveOidcAccount(oidcAccount);
        return oidcAccount;
    }

    @Override
    public InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException {
        try {
            accountRepositoryPort.findInternalAccountByEmail(email);
            throw new EmailAlreadyInUserException("Email " + email + " already in use by another account");
        } catch (AccountNotFoundByEmailException e) {
            return accountRepositoryPort.updateInternalAccountEmail(accountId, email);
        }
    }

    @Override
    public AccountAccessToken mintAccountAccessToken(Account account) {
        return jwtProvider.createAccountAccessToken(account);
    }

    @Override
    public void deleteAccount(AccountId accountId) throws AccountNotFoundByIdException {
        accountRepositoryPort.delete(accountId);
    }

    /*@Override
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
    }*/

    /*
    private Account getValidatedAccount(AccountProvider accountProvider, String email) throws AccountNotFoundByEmailException, WrongProviderException {
        Account account = accountRepositoryPort.findAccountByEmail(email);
        if (account.provider() != accountProvider) {
            throw new WrongProviderException("Account linked to email " + email + "requires an access token provided by an " + accountProvider.name() + " itentiy provider");
        } else {
            return account;
        }
    }*/



}

package thi.cnd.authservice.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.AccountFactory;
import thi.cnd.authservice.domain.AccountServicePort;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.jwt.JwtProvider;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.application.ports.out.repository.AccountRepositoryPort;


@Service
@Validated
@AllArgsConstructor
public class AccountService implements AccountServicePort {

    private final JwtProvider jwtProvider;
    private final AccountRepositoryPort accountRepositoryPort;
    private final AccountFactory accountFactory;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Override
    public InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException {
        InternalAccount internalAccount = accountFactory.buildInternal(email, password);
        return accountRepositoryPort.saveInternalAccount(internalAccount);
    }

    @Override
    public OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException {
        OidcAccount oidcAccount = accountFactory.buildOidc(subject);
        return accountRepositoryPort.saveOidcAccount(oidcAccount);
    }

    @Override
    public InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException, WrongProviderException {
        try {
            accountRepositoryPort.findInternalAccountByEmail(email);
            throw new EmailAlreadyInUserException("Email " + email + " already in use by another account");
        } catch (AccountNotFoundByEmailException e) {
            Account account = accountRepositoryPort.findAccountById(accountId);
            if(! (account instanceof InternalAccount internalAccount)) {
                throw new AccountNotFoundByIdException("Account providers do not match. AccountId belongs to OIDC Account");
            } else {
                internalAccount.setEmail(email);
                InternalAccount updatedAccount = accountRepositoryPort.updateInternalAccount(internalAccount);
                return updatedAccount;
            }
        }
    }

    @Override
    public AccountAccessToken mintAccountAccessToken(Account account) {
        return jwtProvider.createAccountAccessToken(account);
    }

    @Override
    public void validateAccount(AccountId accountId) {
        try {
            Account account = accountRepositoryPort.findAccountById(accountId);
            account.setVerified(true);
            accountRepositoryPort.updateAccount(account);
        } catch (Exception unhandled) {
            logger.error("Exception while validating account with id '" + accountId.toString() + "; Exception: " + unhandled.getMessage());
        }
    }

        @Override
    public void invalidateAccount(AccountId accountId) {
        try {
            Account account = accountRepositoryPort.findAccountById(accountId);
            account.setVerified(false);
            accountRepositoryPort.updateAccount(account);
        } catch (Exception unhandled) {
            logger.error("Exception while invalidating account with id '" + accountId.toString() + "; Exception: " + unhandled.getMessage());
        }
    }

    @Override
    public void deleteAccount(AccountId accountId) throws AccountNotFoundByIdException, AccountStillVerifiedException {
        accountRepositoryPort.delete(accountId);
    }

    @Override
    public void updateInternalAccountPassword(AccountId accountId, String newPlaintextPassword) throws AccountNotFoundByIdException, InvalidPasswordException, WrongProviderException {
        InternalAccount internalAccountWithOldPassword = accountRepositoryPort.findInternalAccountById(accountId);
        InternalAccount updatedInternalAccount = accountFactory.updatePassword(internalAccountWithOldPassword, newPlaintextPassword);
        accountRepositoryPort.updateInternalAccount(updatedInternalAccount);
    }
}
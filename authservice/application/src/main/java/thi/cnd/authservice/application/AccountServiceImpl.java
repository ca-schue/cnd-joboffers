package thi.cnd.authservice.application;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.application.ports.out.mongo.AccountRepository;
import thi.cnd.authservice.application.ports.out.security.PasswordProvider;
import thi.cnd.authservice.application.ports.out.security.TokenProvider;
import thi.cnd.authservice.domain.AccountService;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.*;

import java.time.Instant;


@Service
@Validated
@AllArgsConstructor
class AccountServiceImpl implements AccountService {

    private final TokenProvider tokenProvider;
    private final AccountRepository accountRepository;
    private final PasswordProvider passwordProvider;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public InternalAccount registerNewInternalAccount(String email, String password) throws AccountAlreadyExistsException, InvalidPasswordException {
        InternalAccount internalAccount = this.buildInternalAccount(email, password);
        return accountRepository.saveInternalAccount(internalAccount);
    }

    @Override
    public OidcAccount registerNewOidcAccount(String subject) throws AccountAlreadyExistsException {
        OidcAccount oidcAccount = this.buildOidcAccount(subject);
        return accountRepository.saveOidcAccount(oidcAccount);
    }

    @Override
    public InternalAccount updateInternalAccountEmail(AccountId accountId, String email) throws EmailAlreadyInUserException, AccountNotFoundByIdException, WrongProviderException {
        try {
            accountRepository.findInternalAccountByEmail(email);
            throw new EmailAlreadyInUserException("Email " + email + " already in use by another account");
        } catch (AccountNotFoundByEmailException e) {
            Account account = accountRepository.findAccountById(accountId);
            if(! (account instanceof InternalAccount internalAccount)) {
                throw new AccountNotFoundByIdException("Account providers do not match. AccountId belongs to OIDC Account");
            } else {
                internalAccount.setEmail(email);
                InternalAccount updatedAccount = accountRepository.updateInternalAccount(internalAccount);
                return updatedAccount;
            }
        }
    }

    @Override
    public AccessToken mintAccountAccessToken(Account account) {
        return tokenProvider.createAccountAccessToken(account);
    }

    @Override
    public void validateAccount(AccountId accountId) {
        try {
            Account account = accountRepository.findAccountById(accountId);
            account.setVerified(true);
            accountRepository.updateAccount(account);
        } catch (Exception unhandled) {
            logger.error("Exception while validating account with id '" + accountId.toString() + "; Exception: " + unhandled.getMessage());
        }
    }

        @Override
    public void invalidateAccount(AccountId accountId) {
        try {
            Account account = accountRepository.findAccountById(accountId);
            account.setVerified(false);
            accountRepository.updateAccount(account);
        } catch (Exception unhandled) {
            logger.error("Exception while invalidating account with id '" + accountId.toString() + "; Exception: " + unhandled.getMessage());
        }
    }

    @Override
    public void deleteAccount(AccountId accountId) throws AccountNotFoundByIdException, AccountStillVerifiedException {
        accountRepository.delete(accountId);
    }

    @Override
    public void updateInternalAccountPassword(AccountId accountId, String newPlaintextPassword) throws AccountNotFoundByIdException, InvalidPasswordException, WrongProviderException {
        InternalAccount internalAccountWithOldPassword = accountRepository.findInternalAccountById(accountId);
        InternalAccount updatedInternalAccount = this.updatePassword(internalAccountWithOldPassword, newPlaintextPassword);
        accountRepository.updateInternalAccount(updatedInternalAccount);
    }

    @Override
    public InternalAccount updateLastInternalAccountLogin(String email) throws AccountNotFoundByEmailException {
        return accountRepository.findInternalAccountByEmailAndUpdateLastLogin(email);
    }

    @Override
    public OidcAccount updateLastOidcAccountLogin(String subject) throws AccountNotFoundBySubjectException {
        return accountRepository.findOidcAccountBySubjectAndUpdateLastLogin(subject);
    }

    private OidcAccount buildOidcAccount(String subject) {
        return new OidcAccount(
                new AccountId(),
                Instant.now(),
                subject,
                false
        );
    }

    private InternalAccount buildInternalAccount(String email, String password) throws InvalidPasswordException {
        return new InternalAccount(
                new AccountId(),
                Instant.now(),
                email,
                this.passwordProvider.validatePasswordAndEncode(password),
                false
        );
    }

    private InternalAccount updatePassword(InternalAccount internalAccount, String newPlainTextPassword) throws InvalidPasswordException {
        return new InternalAccount(
                internalAccount.getId(),
                internalAccount.getLastLogin(),
                internalAccount.getEmail(),
                this.passwordProvider.validatePasswordAndEncode(newPlainTextPassword),
                internalAccount.isVerified()
        );
    }

}
package thi.cnd.authservice.secondary.repository.account;

import lombok.AllArgsConstructor;
import org.ietf.jgss.Oid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.exceptions.*;
import thi.cnd.authservice.core.model.account.*;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;
import thi.cnd.authservice.secondary.repository.account.model.AccountDAO;
import thi.cnd.authservice.secondary.repository.account.model.AccountDAOMapper;
import thi.cnd.authservice.secondary.repository.account.model.InternalAccountDAO;
import thi.cnd.authservice.secondary.repository.account.model.OidcAccountDAO;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountMongoDBRepository accountRepository;
    private final InternalAccountMongoDBRepository internalAccountRepository;
    private final OidcAccountMongoDBRepository oidcAccountRepository;
    private final AccountDAOMapper mapper;

    @Override
    public Account saveAccount(Account account) throws AccountAlreadyExistsException {
        try {
            AccountDAO dao = mapper.toAccountDAO(account);
            AccountDAO savedDao = accountRepository.save(dao);
            return mapper.toAccount(savedDao);
        } catch (DuplicateKeyException e) {
            throw new AccountAlreadyExistsException("Account ID already exists or email/subject is already linked to existing account.");
        }
    }

    @Override
    public InternalAccount saveInternalAccount(InternalAccount internalAccount) throws AccountAlreadyExistsException {
        Account savedUpdatedAccount = this.saveAccount(internalAccount);
        if (savedUpdatedAccount.getProvider().equals(internalAccount.getProvider())) {
            return (InternalAccount) savedUpdatedAccount;
        } else {
            throw new RuntimeException("Internal error: An " + savedUpdatedAccount.getProvider().name() + " Account was saved, but an " + internalAccount.getProvider().name() + " Account was given");
        }
    }

    @Override
    public OidcAccount saveOidcAccount(OidcAccount oidcAccount) throws AccountAlreadyExistsException {
        Account savedUpdatedAccount = this.saveAccount(oidcAccount);
        if (savedUpdatedAccount.getProvider().equals(oidcAccount.getProvider())) {
            return (OidcAccount) savedUpdatedAccount;
        } else {
            throw new RuntimeException("Internal error: An " + savedUpdatedAccount.getProvider().name() + " Account was saved, but an " + oidcAccount.getProvider().name() + " Account was given");
        }
    }


    @Override
    public Account updateAccount(Account updatedAccount) throws AccountNotFoundByIdException, WrongProviderException {
        // 1. ID of updatedAccount must exist
        Account foundAccount = findAccountById(updatedAccount.getId());
        // 2. Existing account must have same provider as given Account to be updated
        if (foundAccount.getProvider().equals(updatedAccount.getProvider())) {
            AccountDAO updatedAccountDAO = mapper.toAccountDAO(updatedAccount);
            AccountDAO savedUpdatedAccountDAO = accountRepository.save(updatedAccountDAO);
            // 3. Returned save account must have same provider as given Account to be updated
            return mapper.toAccount(savedUpdatedAccountDAO);
        } else {
            throw new WrongProviderException("Account providers do not match. AccountId of existing Account belongs to OIDC Account");
        }
    }

    @Override
    public InternalAccount updateInternalAccount(InternalAccount updatedInternalAccount) throws AccountNotFoundByIdException, WrongProviderException {
        Account savedUpdatedAccount = this.updateAccount(updatedInternalAccount);
        if(savedUpdatedAccount.getProvider().equals(updatedInternalAccount.getProvider())) {
            return (InternalAccount) savedUpdatedAccount;
        } else {
            throw new RuntimeException("Internal error: An " + savedUpdatedAccount.getProvider().name() + " Account was saved, but an " + updatedInternalAccount.getProvider().name() + " Account was given");
        }
    }

    @Override
    public OidcAccount updateOidcAccount(OidcAccount updatedOidcAccount) throws AccountNotFoundByIdException, WrongProviderException {
        Account savedUpdatedAccount = this.updateAccount(updatedOidcAccount);
        if(savedUpdatedAccount.getProvider().equals(updatedOidcAccount.getProvider())) {
            return (OidcAccount) savedUpdatedAccount;
        } else {
            throw new RuntimeException("Internal error: An " + savedUpdatedAccount.getProvider().name() + " Account was saved, but an " + updatedOidcAccount.getProvider().name() + " Account was given");
        }
    }


    @Override
    public InternalAccount findInternalAccountById(AccountId accountId) throws AccountNotFoundByIdException, WrongProviderException {
        Account foundAccount = findAccountById(accountId);
        if((foundAccount instanceof InternalAccount internalAccount) && foundAccount.getProvider().equals(AccountProvider.INTERNAL)) {
            return internalAccount;
        } else {
            throw new WrongProviderException("Account providers do not match. AccountId belongs to OIDC Account");
        }
    }

    @Override
    public OidcAccount findOidcAccountById(AccountId accountId) throws AccountNotFoundByIdException, WrongProviderException {
        Account foundAccount = findAccountById(accountId);
        if((foundAccount instanceof OidcAccount oidcAccount) && foundAccount.getProvider().equals(AccountProvider.OIDC)) {
            return oidcAccount;
        } else {
            throw new WrongProviderException("Account providers do not match. AccountId belongs to Internal Account");
        }
    }


    @Override
    public Account findAccountById(AccountId accountId) throws AccountNotFoundByIdException {
        AccountDAO accountDao = accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundByIdException("No account linked to id " + accountId.toString()));
        return mapper.toAccount(accountDao);
    }

    @Override
    public void delete(AccountId id) throws AccountNotFoundByIdException{
        accountRepository.deleteOneById(id).orElseThrow(() -> new AccountNotFoundByIdException("Could not delete account. No account linked to email"));
    }

    @Override
    public OidcAccount findOidcAccountBySubjectAndUpdateLastLogin(String subject) throws AccountNotFoundBySubjectException {
        OidcAccountDAO oidcAccountDAO = oidcAccountRepository
                .findBySubjectAndUpdateLastLogin(subject)
                .orElseThrow(() -> new AccountNotFoundBySubjectException("No account linked to subject " + subject));
        return mapper.toOidcAccount(oidcAccountDAO);
    }

    @Override
    public InternalAccount findInternalAccountByEmailAndUpdateLastLogin(String email) throws AccountNotFoundByEmailException {
        InternalAccountDAO internalAccountDAO = internalAccountRepository
                .findByEmailAndUpdateLastLogin(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toInternalAccount(internalAccountDAO);
    }

    @Override
    public InternalAccount findInternalAccountByEmail(String email) throws AccountNotFoundByEmailException {
        InternalAccountDAO accountDAO = internalAccountRepository
                .findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toInternalAccount(accountDAO);
    }

}

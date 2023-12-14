package thi.cnd.authservice.adapters.out.mongo.accounts;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.AccountDAO;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.AccountDaoMapper;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.InternalAccountDAO;
import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.OidcAccountDAO;
import thi.cnd.authservice.adapters.out.mongo.accounts.internal.InternalAccountMongoRepository;
import thi.cnd.authservice.adapters.out.mongo.accounts.oidc.OidcAccountMongoRepository;
import thi.cnd.authservice.application.ports.out.repository.AccountRepository;
import thi.cnd.authservice.domain.exceptions.*;
import thi.cnd.authservice.domain.model.account.*;

@Component
@AllArgsConstructor
class AllAccountsMongoRepositoryImpl implements AccountRepository {

    private final AccountMongoRepository accountRepository;
    private final InternalAccountMongoRepository internalAccountRepository;
    private final OidcAccountMongoRepository oidcAccountRepository;
    private final AccountDaoMapper mapper;

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
    public void delete(AccountId id) throws AccountNotFoundByIdException, AccountStillVerifiedException {
        Account account = findAccountById(id);
        if (account.isVerified()) {
            throw new AccountStillVerifiedException("User Profile linked to account. Please delete user profile first");
        }
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

package thi.cnd.authservice.secondary.repository.account;

import lombok.AllArgsConstructor;
import org.ietf.jgss.Oid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.core.model.account.InternalAccount;
import thi.cnd.authservice.core.model.account.OidcAccount;
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
    public InternalAccount saveInternalAccount(InternalAccount internalAccount) throws AccountAlreadyExistsException {
        try {
            InternalAccountDAO dao = mapper.toInternalAccountDAO(internalAccount);
            InternalAccountDAO savedDao = accountRepository.save(dao);
            return mapper.toInternalAccount(savedDao);
        } catch (DuplicateKeyException e) {
            throw new AccountAlreadyExistsException("Account ID already exists or email/subject is already linked to existing account.");
        }
    }

    @Override
    public OidcAccount saveOidcAccount(OidcAccount oidcAccount) throws AccountAlreadyExistsException {
        try {
            OidcAccountDAO dao = mapper.toOidcAccountDAO(oidcAccount);
            OidcAccountDAO savedDao = accountRepository.save(dao);
            return mapper.toOidcAccount(savedDao);
        } catch (DuplicateKeyException e) {
            throw new AccountAlreadyExistsException("Account ID already exists or email/subject is already linked to existing account.");
        }
    }

    @Override
    public InternalAccount findInternalAccountByEmailAndUpdateLastLogin(String email) throws AccountNotFoundByEmailException {
        InternalAccountDAO internalAccountDAO = internalAccountRepository
                .findByEmailAndUpdateLastLogin(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toInternalAccount(internalAccountDAO);
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
    public InternalAccount findInternalAccountByEmail(String email) throws AccountNotFoundByEmailException {
        InternalAccountDAO accountDAO = internalAccountRepository
                .findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toInternalAccount(accountDAO);
    }

    @Override
    public InternalAccount updateInternalAccountEmail(AccountId userId, String email) throws AccountNotFoundByIdException {
        InternalAccountDAO updatedInternalAccountDAO = internalAccountRepository.findByIdAndUpdateEmail(userId, email)
                .orElseThrow(() -> new AccountNotFoundByIdException("Could not find user for id " + userId));
        InternalAccount internalAccount = mapper.toInternalAccount(updatedInternalAccountDAO);
        return  internalAccount;

    }
}

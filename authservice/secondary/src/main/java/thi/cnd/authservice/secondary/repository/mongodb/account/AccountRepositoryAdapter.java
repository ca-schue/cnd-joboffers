package thi.cnd.authservice.secondary.repository.mongodb.account;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.authservice.core.exceptions.AccountAlreadyExistsException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByEmailException;
import thi.cnd.authservice.core.exceptions.AccountNotFoundByIdException;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountId;
import thi.cnd.authservice.core.ports.secondary.AccountRepositoryPort;
import thi.cnd.authservice.secondary.repository.mongodb.account.model.AccountDAO;
import thi.cnd.authservice.secondary.repository.mongodb.account.model.AccountDAOMapper;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountMongoDBRepository repository;
    private final AccountDAOMapper mapper;

    @Override
    public Account save(Account account) throws AccountAlreadyExistsException {
        try {
            AccountDAO dao = mapper.toDAO(account);
            AccountDAO savedDao = repository.save(dao);
            return mapper.toAccount(savedDao);
        } catch (DuplicateKeyException e) {
            throw new AccountAlreadyExistsException("Account ID already exists or email is already linked to existing account.");
        }
    }

    @Override
    public Account findAccountByEmailAndUpdateLastLogin(String email) throws AccountNotFoundByEmailException {
        AccountDAO accountDAO = repository
                .findByEmailAndUpdateLastLogin(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toAccount(accountDAO);
    }

    @Override
    public Account findAccountById(AccountId accountId) throws AccountNotFoundByIdException {
        AccountDAO accountDao = repository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundByIdException("No account linked to id " + accountId.toString()));
        return mapper.toAccount(accountDao);

    }

    @Override
    public void delete(AccountId id) throws AccountNotFoundByIdException{
        repository.deleteOneById(id).orElseThrow(() -> new AccountNotFoundByIdException("Could not delete account. No account linked to email"));
    }

    @Override
    public Account findAccountByEmail(String email) throws AccountNotFoundByEmailException {
        AccountDAO accountDAO = repository
                .findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundByEmailException("No account linked to email " + email));
        return mapper.toAccount(accountDAO);
    }

    /*@Override
    public Account updateEmail(AccountId userId, String email) {
        return repository.updateEmailById(userId, email)
                .map(mapper::toAccount)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Could not find user for id " + userId));
    }*/
}

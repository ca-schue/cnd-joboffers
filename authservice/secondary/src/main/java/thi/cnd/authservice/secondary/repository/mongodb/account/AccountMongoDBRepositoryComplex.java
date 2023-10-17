package thi.cnd.authservice.secondary.repository.mongodb.account;

import jakarta.validation.constraints.NotBlank;
import thi.cnd.authservice.secondary.repository.mongodb.account.model.AccountDAO;

import java.util.Optional;

public interface AccountMongoDBRepositoryComplex {

    public Optional<AccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email);

/*  public Optional<Query> updateEmailById(@NotNull AccountId accountId, @Email String email) {
        var query = new Query(Criteria.where("id").is(accountId));
        var update = Update.update("email", email);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, AccountDAO.class));
    } */
}

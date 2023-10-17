package thi.cnd.authservice.secondary.repository.mongodb.account;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.secondary.repository.mongodb.account.model.AccountDAO;

import java.time.Instant;
import java.util.Optional;

@Repository
@Validated
@RequiredArgsConstructor
public class AccountMongoDBRepositoryComplexImpl implements AccountMongoDBRepositoryComplex{
    private final MongoTemplate mongoTemplate;

    public Optional<AccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email) {
        var query = new Query(Criteria.where("email").is(email));
        var update = Update.update("lastLogin", Instant.now());
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, AccountDAO.class));
    }

/*  public Optional<Query> updateEmailById(@NotNull AccountId accountId, @Email String email) {
        var query = new Query(Criteria.where("id").is(accountId));
        var update = Update.update("email", email);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, AccountDAO.class));
    } */
}

package thi.cnd.userservice.adapters.out.repository.accounts.internal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.userservice.adapters.out.repository.accounts.model.InternalAccountDAO;

import java.time.Instant;
import java.util.Optional;

@Repository
@Validated
@RequiredArgsConstructor
public class ComplexInternalAccountMongoRepositoryImpl implements ComplexInternalAccountMongoRepository {
    private final MongoTemplate mongoTemplate;
    FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

    public Optional<InternalAccountDAO> findByEmailAndUpdateLastLogin(@NotBlank String email) {
        var query = new Query(Criteria.where("email").is(email));
        var update = Update.update("lastLogin", Instant.now());
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, findAndModifyOptions, InternalAccountDAO.class));
    }

    public Optional<InternalAccountDAO> findByIdAndUpdateEmail(@NotNull AccountId accountId, @Email String newEmail) {
        var query = new Query(Criteria.where("id").is(accountId));
        var update = Update.update("email", newEmail);
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, findAndModifyOptions, InternalAccountDAO.class));
    }
}

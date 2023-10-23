package thi.cnd.authservice.secondary.repository.account;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.secondary.repository.account.model.InternalAccountDAO;
import thi.cnd.authservice.secondary.repository.account.model.OidcAccountDAO;

import java.time.Instant;
import java.util.Optional;

@Repository
@Validated
@RequiredArgsConstructor
public class OidcAccountMongoDBRepositoryComplexImpl implements OidcAccountMongoDBRepositoryComplex{
    private final MongoTemplate mongoTemplate;
    FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
    @Override
    public Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject) {
        var query = new Query(Criteria.where("subject").is(subject));
        var update = Update.update("lastLogin", Instant.now());
        return Optional.ofNullable(mongoTemplate.findAndModify(query, update, findAndModifyOptions, OidcAccountDAO.class));
    }
}

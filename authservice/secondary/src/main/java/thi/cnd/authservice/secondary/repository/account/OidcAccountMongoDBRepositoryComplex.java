package thi.cnd.authservice.secondary.repository.account;

import thi.cnd.authservice.secondary.repository.account.model.OidcAccountDAO;

import java.util.Optional;

public interface OidcAccountMongoDBRepositoryComplex {

    Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject);
}

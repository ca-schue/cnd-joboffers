package thi.cnd.userservice.adapters.out.repository.account;

import thi.cnd.userservice.adapters.out.repository.account.model.OidcAccountDAO;

import java.util.Optional;

public interface OidcAccountMongoDBRepositoryComplex {

    Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject);
}

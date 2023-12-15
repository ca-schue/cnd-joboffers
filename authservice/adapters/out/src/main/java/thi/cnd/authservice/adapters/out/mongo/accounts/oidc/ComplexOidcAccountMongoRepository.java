package thi.cnd.authservice.adapters.out.mongo.accounts.oidc;

import thi.cnd.authservice.adapters.out.mongo.accounts.DAOs.OidcAccountDAO;

import java.util.Optional;

public interface ComplexOidcAccountMongoRepository {

    Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject);
}
package thi.cnd.authservice.adapters.out.repository.accounts.oidc;

import thi.cnd.authservice.adapters.out.repository.accounts.DAOs.OidcAccountDAO;

import java.util.Optional;

public interface ComplexOidcAccountMongoRepository {

    Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject);
}

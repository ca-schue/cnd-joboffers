package thi.cnd.userservice.adapters.out.repository.accounts.oidc;

import thi.cnd.userservice.adapters.out.repository.accounts.model.OidcAccountDAO;

import java.util.Optional;

public interface ComplexOidcAccountMongoRepository {

    Optional<OidcAccountDAO> findBySubjectAndUpdateLastLogin(String subject);
}

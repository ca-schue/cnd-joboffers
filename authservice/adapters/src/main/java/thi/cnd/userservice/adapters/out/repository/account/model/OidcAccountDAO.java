package thi.cnd.userservice.adapters.out.repository.account.model;

import lombok.Getter;
import lombok.Setter;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.time.Instant;

@Getter
@Setter
public class OidcAccountDAO extends AccountDAO {

    private String subject;

    // TODO: Set provider here as final

    public OidcAccountDAO(AccountId id, String provider, Instant lastLogin, String subject) {
        this.setId(id);
        this.setProvider(provider);
        this.setLastLogin(lastLogin);
        this.subject = subject;
    }
}

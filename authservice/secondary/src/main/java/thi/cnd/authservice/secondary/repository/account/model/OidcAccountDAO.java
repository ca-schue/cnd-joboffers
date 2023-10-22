package thi.cnd.authservice.secondary.repository.account.model;

import lombok.Getter;
import lombok.Setter;
import thi.cnd.authservice.core.model.account.AccountId;

import java.time.Instant;

@Getter
@Setter
public class OidcAccountDAO extends AccountDAO {

    private String subject;

    public OidcAccountDAO(AccountId id, String provider, Instant lastLogin, String subject) {
        super(id, provider, lastLogin);
        this.subject = subject;
    }
}

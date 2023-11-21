package thi.cnd.userservice.adapters.out.repository.account.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.time.Instant;

@Getter
@Setter
public class InternalAccountDAO extends AccountDAO {

    @Indexed(unique = true)
    private String email;

    private String encryptedPassword;

    public InternalAccountDAO(AccountId id, String provider, Instant lastLogin, String email, String encryptedPassword, boolean verified) {
        this.setId(id);
        this.setProvider(provider);
        this.setLastLogin(lastLogin);
        this.setVerified(verified);
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

}

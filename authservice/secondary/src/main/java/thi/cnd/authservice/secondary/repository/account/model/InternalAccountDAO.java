package thi.cnd.authservice.secondary.repository.account.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import thi.cnd.authservice.core.model.account.AccountId;

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

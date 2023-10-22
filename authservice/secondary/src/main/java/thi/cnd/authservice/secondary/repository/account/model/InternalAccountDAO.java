package thi.cnd.authservice.secondary.repository.account.model;

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

    public InternalAccountDAO(AccountId id, String provider, Instant lastLogin, String email, String encryptedPassword) {
        super(id, provider, lastLogin);
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }
}

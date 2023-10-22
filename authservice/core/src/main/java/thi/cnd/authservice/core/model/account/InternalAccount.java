package thi.cnd.authservice.core.model.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InternalAccount extends Account {

    @NotBlank String email;
    String encryptedPassword; // can be blank (better: seperate two classes Internal/OidcAccount with and without PW)

    public InternalAccount(@NotNull AccountId id, @NotNull AccountProvider provider, @NotNull Instant lastLogin, @NotNull String email, @NotNull String encryptedPassword) {
        super(id, provider, lastLogin);
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }
}

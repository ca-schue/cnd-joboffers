package thi.cnd.authservice.domain.model.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class InternalAccount extends Account {

    @NotBlank String email;
    String encryptedPassword;
    final AccountProvider provider = AccountProvider.INTERNAL;

    public InternalAccount(@NotNull AccountId id, @NotNull Instant lastLogin, @NotNull String email, @NotNull String encryptedPassword, @NotNull boolean verified) {
        this.setId(id);
        this.setLastLogin(lastLogin);
        this.setVerified(verified);
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }
}

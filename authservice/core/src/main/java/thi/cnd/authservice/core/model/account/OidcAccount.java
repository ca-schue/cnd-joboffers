package thi.cnd.authservice.core.model.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Validated
@Getter
@Setter
public class OidcAccount extends Account {

    private final String subject;
    final AccountProvider provider = AccountProvider.OIDC;

    public OidcAccount(@NotNull AccountId id, @NotNull Instant lastLogin, @NotNull String subject, @NotNull boolean verified) {
        this.setId(id);
        this.setLastLogin(lastLogin);
        this.subject = subject;
        this.setVerified(verified);
    }
}

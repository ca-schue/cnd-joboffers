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

    public OidcAccount(@NotNull AccountId id, @NotNull AccountProvider provider, @NotNull Instant lastLogin, @NotNull String subject) {
        super(id, provider, lastLogin);
        this.subject = subject;
    }
}

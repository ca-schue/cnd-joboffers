package thi.cnd.authservice.domain.model.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
//@AllArgsConstructor
public abstract class Account {
    @NotNull AccountId id;
    @NotNull AccountProvider provider;
    @NotNull Instant lastLogin;
    @NotNull boolean verified;
}

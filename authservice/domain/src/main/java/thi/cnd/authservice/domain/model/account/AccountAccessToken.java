package thi.cnd.authservice.domain.model.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;

public record AccountAccessToken(
        @NotNull Account account,
        @NotBlank Jwt signedAccountJwt
) {
}

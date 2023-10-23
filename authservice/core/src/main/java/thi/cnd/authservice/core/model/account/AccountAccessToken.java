package thi.cnd.authservice.core.model.account;

import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;

public record AccountAccessToken(
        @NotNull Account account,
        @NotBlank Jwt signedAccountJwt
) {
}

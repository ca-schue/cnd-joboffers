package thi.cnd.authservice.domain.model.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;

public record ClientAccessToken(
        @NotNull Client client,
        @NotBlank Jwt signedClientJwt
) {
}
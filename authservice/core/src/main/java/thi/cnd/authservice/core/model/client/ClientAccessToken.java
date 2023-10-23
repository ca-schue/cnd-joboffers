package thi.cnd.authservice.core.model.client;

import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.core.model.account.Account;

public record ClientAccessToken(
        @NotNull Client client,
        @NotBlank Jwt signedClientJwt
) {
}
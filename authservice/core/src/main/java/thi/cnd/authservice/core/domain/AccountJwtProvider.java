package thi.cnd.authservice.core.domain;

import io.jsonwebtoken.JwtBuilder;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.model.Account;
import thi.cnd.authservice.core.model.AccountAccessToken;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@AllArgsConstructor
public class AccountJwtProvider {

    private final JwtConfig jwtConfig;
    private final JwtBuilder jwtBuilder;
    public AccountAccessToken createJwt(Account account) {
        var now = Instant.now();
        var issuedAt = Date.from(now);
        var validUntil = now.plusSeconds(jwtConfig.getValidityInSeconds());

        var accessToken = jwtBuilder
                .setSubject(account.id().toString())
                .setHeader(Map.of("kid", jwtConfig.getKeyId())) // Set the key id
                .setIssuedAt(issuedAt)
                .setNotBefore(issuedAt)
                .setExpiration(Date.from(validUntil))
                .setIssuer(jwtConfig.getIssuer())
                // .addClaims(Map.of(JwtConstants.SCOPE_TYPE_CLAIM_NAME, JwtConstants.USER)) TODO: User-specific scopes
                .addClaims(Map.of(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.USER)) // Set the access token type to 'user'
                .compact();

        return new AccountAccessToken(account, accessToken, validUntil);


    }
}

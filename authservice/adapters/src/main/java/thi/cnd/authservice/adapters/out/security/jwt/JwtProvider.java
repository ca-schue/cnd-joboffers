package thi.cnd.authservice.adapters.out.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.application.ports.out.security.TokenProvider;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.domain.model.client.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@Validated
@Component
public class JwtProvider implements TokenProvider {

    @Qualifier("JwkSetAdapterOut")
    private final JwkSet jwkSet;
    private final String issuer;
    private final long validityInSeconds;

    public JwtProvider(
            @Value("${jwt-config.issuer}") String issuer,
            @PositiveOrZero @Value("${jwt-config.validityInSeconds}") long validityInSeconds,
            JwkSet jwkSet
            ) {
        this.issuer = issuer;
        this.validityInSeconds = validityInSeconds;
        this.jwkSet = jwkSet;
    }

    public AccountAccessToken createAccountAccessToken(Account account) {
        JwtClaimsSet accountJwtClaims = createAccountClaims(account);
        Jwt signedAccountJwt = generateSignedJwt(accountJwtClaims);
        return new AccountAccessToken(account, signedAccountJwt);
    }

    public ClientAccessToken createClientAccessToken(Client client) {
        JwtClaimsSet clientJwtClaims = createClientClaims(client);
        Jwt signedAccountJwt = generateSignedJwt(clientJwtClaims);
        return new ClientAccessToken(client, signedAccountJwt);
    }

    private JwtClaimsSet createAccountClaims(Account account) {
        return JwtClaimsSet.builder()
                .claim(JwtClaims.subjectTypeClaimName, JwtClaims.subjectTypeAccount)
                .claim(JwtClaims.verifiedClaimName, account.isVerified())
                .audience(Arrays.asList("user-service", "career-service", "auth-service", "notification-service"))
                .subject(account.getId().id().toString())
                .build();
    }

    private JwtClaimsSet createClientClaims(Client client) {
        return JwtClaimsSet.builder()
                .claim(JwtClaims.subjectTypeClaimName, JwtClaims.subjectTypeClient)
                .subject(client.name())
                .audience(client.audiences().stream().toList())
                .claim("scope", client.scopes() == null ? Collections.emptyList() : client.scopes().stream().toList())
                .build();
    }

    private Jwt generateSignedJwt(JwtClaimsSet specificJwtClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(this.validityInSeconds * 1000);

        NimbusJwtEncoder nimbusJwtEncoder = new NimbusJwtEncoder(this.jwkSet.getJwkSet());

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.from(this.jwkSet.getRsaSigningAlgorithm());
        JwsHeader jwsHeader = JwsHeader.with(signatureAlgorithm).build();

        JwtClaimsSet claimsSet = JwtClaimsSet.from(specificJwtClaims)
                .issuer(this.issuer)
                .issuedAt(now)
                .expiresAt(expiration)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, claimsSet);

        return nimbusJwtEncoder.encode(jwtEncoderParameters);
    }
}
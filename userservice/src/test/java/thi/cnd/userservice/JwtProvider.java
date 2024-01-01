package thi.cnd.userservice;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@Validated
@Component
class JwtProvider {

    private final JWKSource<SecurityContext> jwkSource;

    @Qualifier("JwkSetTest")
    private final JwkSet jwkSet;
    private final String issuer;
    private final long validityInSeconds;

    public JwtProvider(
            @Value("${jwt-config.issuer}") String issuer,
            @PositiveOrZero @Value("${jwt-config.validityInSeconds}") long validityInSeconds,
            JWKSource<SecurityContext> jwkSource,
            JwkSet jwkSet
            ) {
        this.issuer = issuer;
        this.validityInSeconds = validityInSeconds;
        this.jwkSource = jwkSource;
        this.jwkSet = jwkSet;
    }

    public Jwt mintAccountJwt(String accountId, boolean isVerified) {
        return generateSignedJwt(createAccountClaims(accountId, isVerified));
    }

    public Jwt mintClientJwt(String clientName, String [] audiences , String [] scopes) {
        return generateSignedJwt(createClientClaims(clientName, audiences, scopes));
    }

    private JwtClaimsSet createAccountClaims(String accountId, boolean isVerified) {
        return JwtClaimsSet.builder()
                .claim(JwtClaims.subjectTypeClaimName, JwtClaims.subjectTypeAccount)
                .claim(JwtClaims.verifiedClaimName, isVerified)
                .audience(Arrays.asList("user-service", "career-service", "auth-service", "notification-service"))
                .subject(accountId)
                .build();
    }

    private JwtClaimsSet createClientClaims(String clientName, String [] audiences , String [] scopes) {
        return JwtClaimsSet.builder()
                .claim(JwtClaims.subjectTypeClaimName, JwtClaims.subjectTypeClient)
                .subject(clientName)
                .audience(Arrays.asList(audiences))
                .claim("scope", scopes.length == 0 ? Collections.emptyList() : Arrays.asList(scopes))
                .build();
    }

    private Jwt generateSignedJwt(JwtClaimsSet specificJwtClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(this.validityInSeconds * 1000);

        NimbusJwtEncoder nimbusJwtEncoder = new NimbusJwtEncoder(jwkSource);

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
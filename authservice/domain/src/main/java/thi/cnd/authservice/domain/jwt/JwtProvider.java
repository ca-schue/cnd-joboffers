package thi.cnd.authservice.domain.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.domain.model.client.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;
    private final JwtKeys jwtKeys;

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
        JwtClaimsSet accountJwtClaims = JwtClaimsSet.builder()
                .claim(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.ACCOUNT)
                .claim(JwtConstants.VERIFIED_CLAIM_NAME, account.isVerified())
                .audience(Arrays.asList("user-service", "career-service", "auth-service", "notification-service"))
                .subject(account.getId().id().toString())
                .build();
        return accountJwtClaims;
    }

    private JwtClaimsSet createClientClaims(Client client) {
        JwtClaimsSet clientJwtClaims = JwtClaimsSet.builder()
                .claim(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.CLIENT)
                .subject(client.name())
                .audience(client.audiences().stream().toList())
                .claim("scope", client.scopes() == null ? Collections.emptyList() : client.scopes().stream().toList())
                .build();
        return clientJwtClaims;
    }

    public Jwt generateSignedJwt(JwtClaimsSet specificJwtClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtConfig.getValidityInSeconds() * 1000);

        NimbusJwtEncoder nimbusJwtEncoder = new NimbusJwtEncoder(jwtKeys.jwkSource());

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.from(jwtConfig.getSigningAlgorithm());
        JwsHeader jwsHeader = JwsHeader.with(signatureAlgorithm).build();

        JwtClaimsSet claimsSet = JwtClaimsSet.from(specificJwtClaims)
                .issuer(jwtConfig.getIssuer())
                .issuedAt(now)
                .expiresAt(expiration)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, claimsSet);

        Jwt signedJwt = nimbusJwtEncoder.encode(jwtEncoderParameters);

        return signedJwt;
    }
}
package thi.cnd.authservice.core.domain.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountAccessToken;

import java.time.Instant;
import java.util.Date;
import java.util.Map;



/*
@Component
@AllArgsConstructor
public class AccountJwtProvider {

    private final JwtConfig jwtConfig;
    public AccountAccessToken createJwt(Account account) {
        try {
            Date now = new Date();
            Instant validUntil = Instant.now().plusSeconds(jwtConfig.getValidityInSeconds());

            JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                    .issuer(jwtConfig.getIssuer())
                    .subject(account.getId().toString()) // Replace with the appropriate subject
                    .issueTime(now)
                    .expirationTime(Date.from(validUntil))
                    //.jwtID(jwtConfig.getKeyId())
                    .claim(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.ACCOUNT)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader
                            .Builder(JWSAlgorithm.parse(jwtConfig.getRsaJwk().getAlgorithm().toString()))
                            .keyID(jwtConfig.getRsaJwk().getKeyID())
                            .build(),
                    jwtClaims);

            JWSSigner signer = new RSASSASigner(jwtConfig.getRsaJwk());
            signedJWT.sign(signer);

            return new AccountAccessToken(account, signedJWT, validUntil);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signed JWT.", e);
        }
    }
}*/

/*
@Component
@AllArgsConstructor
public class AccountJwtProvider {

    private final JwtConfig jwtConfig;
    private final JwtBuilder jwtBuilder = Jwts.builder();
    public AccountAccessToken createJwt(Account account) {
        var now = Instant.now();
        var issuedAt = Date.from(now);
        var validUntil = now.plusSeconds(jwtConfig.getValidityInSeconds());

        JwtClaimsSet claims = null;
        JWKSource<> jwkSource = new ImmutableJWKSet(new JWKSet());

        JwtBuilder accessToken = jwtBuilder
                .setSubject(account.getId().toString())
                .setHeaderParam("kid", jwtConfig.getKeyId()) // Set the key id
                //.setHeaderParam("alg", jwtConfig.getSigningAlgorithm()) // Set the key id
                .setIssuedAt(issuedAt)
                .setNotBefore(issuedAt)
                .setExpiration(Date.from(validUntil))
                .setIssuer(jwtConfig.getIssuer())
                // .addClaims(Map.of(JwtConstants.SCOPE_TYPE_CLAIM_NAME, JwtConstants.USER)) TODO: User-specific scopes
                .addClaims(Map.of(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.ACCOUNT)) // Set the access token type to 'user'
                .signWith(jwtConfig.getPrivateKey(),jwtConfig.getSigningAlgorithm());


        String jwt = accessToken.compact();

        return new AccountAccessToken(account, accessToken, validUntil);
    }
}*/

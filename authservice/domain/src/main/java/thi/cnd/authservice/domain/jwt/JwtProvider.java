package thi.cnd.authservice.domain.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.domain.model.account.*;
import thi.cnd.authservice.domain.model.client.*;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Validated
@Getter
@ConfigurationProperties(prefix = "jwt-config")
public class JwtProvider {

    private final String issuer;
    private final String keyId;
    private final long validityInSeconds;
    private final String keyAlgorithm = "RSA";
    private final String signingAlgorithm;

    private final JWKSource jwkSet;

    public JwtProvider(@NotBlank String issuer, @NotBlank String rsaPublicKeyBase64, @NotBlank String rsaPrivateKeyBase64, @NotBlank String keyId, @PositiveOrZero long validityInSeconds, @NotBlank String rsaSigningAlgorithm) {
        this.signingAlgorithm = rsaSigningAlgorithm;
        this.issuer = issuer;
        this.keyId = keyId;
        this.validityInSeconds = validityInSeconds;
        this.jwkSet = buildJwk(rsaPublicKeyBase64, rsaPrivateKeyBase64);
    }

    private JWKSource buildJwk(String rsaPublicKeyBase64, String rsaPrivateKeyBase64) {
        PublicKey publicKey = buildPublicKey(rsaPublicKeyBase64, this.keyAlgorithm);
        PrivateKey privateKey = buildPrivateKey(rsaPrivateKeyBase64, this.keyAlgorithm);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        RSAKey rsaKeyPair = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .algorithm(JWSAlgorithm.parse(this.signingAlgorithm))
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(keyId)
                .issueTime(Date.from(Instant.now()))
                .build();
        JWKSet jwkSet = new JWKSet(rsaKeyPair);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static PublicKey buildPublicKey(String publicKey, String keyAlgorithm) {
        try {
            byte[] publicBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Could not parse public key.", e);
        }
    }

    private static PrivateKey buildPrivateKey(String privateKey, String keyAlgorithm) {
        try {
            byte[] privateBytes = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Could not parse private key.", e);
        }
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
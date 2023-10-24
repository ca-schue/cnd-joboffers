package thi.cnd.authservice.core.domain.jwt;
/**
 * Loads all jwt configuration from the application config.
 */

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Validated
@Getter
@ConfigurationProperties(prefix = "jwt-config")
public class JwtConfig {
    private final String issuer;
    private final List<String> oidcIssuersDiscoveryEndpoints;
    private final String keyId;
    private final long validityInSeconds;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final RSAKey RsaJwk;
    private final String keyAlgorithm;
    private final String signingAlgorithm;
    public JwtConfig(@NotBlank String issuer, List<String> oidcIssuersDiscoveryEndpoints, @NotBlank String publicKeyBase64, @NotBlank String privateKeyBase64, @NotBlank String keyId, @PositiveOrZero long validityInSeconds, @NotBlank String keyAlgorithm, @NotBlank String signingAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        this.signingAlgorithm = signingAlgorithm;
        this.issuer = issuer;
        this.keyId = keyId;
        this.validityInSeconds = validityInSeconds;
        this.oidcIssuersDiscoveryEndpoints = oidcIssuersDiscoveryEndpoints;

        this.publicKey = buildPublicKey(publicKeyBase64, this.keyAlgorithm);
        this.privateKey = buildPrivateKey(privateKeyBase64, this.keyAlgorithm);
        KeyPair keyPair = new KeyPair(this.publicKey, this.privateKey);
        this.RsaJwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .algorithm(JWSAlgorithm.parse(signingAlgorithm))
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(keyId)
                .issueTime(Date.from(Instant.now()))
                .build();
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

}

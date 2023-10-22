package thi.cnd.authservice.core.domain.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

/**
 * Loads all jwt configuration from the application config.
 */
@Getter
@ConfigurationProperties(prefix = "jwt-config")
public class JwtConfig {
    private final String issuer;
    private final List<String> oidcIssuers;
    private final String publicKeyBase64;
    private final String privateKeyBase64;
    private final String keyId;
    private final long validityInSeconds;
    private final String keyAlgorithm;
    private final SignatureAlgorithm signingAlgorithm;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public JwtConfig(String issuer, List<String> oidcIssuers, String publicKey, String privateKey, String keyId, long validityInSeconds, String keyAlgorithm,
                     String signingAlgorithm) {
        this.issuer = issuer;
        this.publicKeyBase64 = publicKey;
        this.privateKeyBase64 = privateKey;
        this.keyId = keyId;
        this.validityInSeconds = validityInSeconds;
        this.keyAlgorithm = keyAlgorithm;
        this.signingAlgorithm = SignatureAlgorithm.valueOf(signingAlgorithm);
        this.oidcIssuers = oidcIssuers;
        this.publicKey = buildPublicKey(getPublicKeyBase64(), getKeyAlgorithm());
        this.privateKey = buildPrivateKey(getPrivateKeyBase64(), getKeyAlgorithm());

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

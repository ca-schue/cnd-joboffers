package thi.cnd.authservice.core.domain.jwt;
/**
 * Loads all jwt configuration from the application config.
 */

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    public JwtConfig(String issuer, List<String> oidcIssuersDiscoveryEndpoints, String publicKeyBase64, String privateKeyBase64, String keyId, long validityInSeconds, String keyAlgorithm, String signingAlgorithm) {

        this.keyAlgorithm = keyAlgorithm;
        this.signingAlgorithm = signingAlgorithm;
        this.publicKey = buildPublicKey(publicKeyBase64, this.keyAlgorithm);
        this.privateKey = buildPrivateKey(privateKeyBase64, this.keyAlgorithm);
        this.issuer = issuer;
        this.keyId = keyId;
        this.validityInSeconds = validityInSeconds;
        this.oidcIssuersDiscoveryEndpoints = oidcIssuersDiscoveryEndpoints;

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

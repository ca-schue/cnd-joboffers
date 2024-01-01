package thi.cnd.userservice;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
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


@Validated
@Component("JwkSetTest")
@Configuration
class JwkSet {

    private final String rsaPublicKeyBase64;
    private final String rsaPrivateKeyBase64;
    private final String rsaSigningAlgorithm;
    private final String keyId;

    public JwkSet(
            @Value("${jwk.rsaPublicKeyBase64}") String rsaPublicKeyBase64,
            @Value("${jwk.rsaPrivateKeyBase64}") String rsaPrivateKeyBase64,
            @Value("${jwk.rsaSigningAlgorithm}") String rsaSigningAlgorithm,
            @Value("${jwk.keyId}") String keyId
            ){
        this.rsaPublicKeyBase64 = rsaPublicKeyBase64;
        this.rsaPrivateKeyBase64 = rsaPrivateKeyBase64;
        this.rsaSigningAlgorithm = rsaSigningAlgorithm;
        this.keyId = keyId;
    }

    public String getRsaSigningAlgorithm() {
        return rsaSigningAlgorithm;
    }

    private JWKSource buildJwk(String rsaPublicKeyBase64, String rsaPrivateKeyBase64) {
        String keyAlgorithm = "RSA";
        PublicKey publicKey = buildPublicKey(rsaPublicKeyBase64, keyAlgorithm);
        PrivateKey privateKey = buildPrivateKey(rsaPrivateKeyBase64, keyAlgorithm);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        RSAKey rsaKeyPair = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .algorithm(JWSAlgorithm.parse(this.rsaSigningAlgorithm))
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyUse(KeyUse.SIGNATURE)
                .keyID(this.keyId)
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

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        return this.buildJwk(this.rsaPublicKeyBase64, this.rsaPrivateKeyBase64);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(buildPublicKey()).build();
    }

    private RSAPublicKey buildPublicKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(rsaPublicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            // Fügen Sie hier eine geeignete Exception-Behandlung hinzu
            e.printStackTrace();
            return null;
        }
    }
}

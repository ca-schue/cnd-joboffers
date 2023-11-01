package thi.cnd.authservice.test.core.unit;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestConfiguration;
import thi.cnd.authservice.core.domain.jwt.JwtConfig;
import thi.cnd.authservice.core.domain.jwt.JwtKeys;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;

@ExtendWith(MockitoExtension.class)
public class JwtProviderUnitTests {

    // TODO: Replace hard coded constructor parameters with @TestConfiguration (valid.yaml) / (invalid.yaml)

    // TODO: Integration Test: Check if Resource Server and Authorization Server get correct JWKSource

    @Test
    public void jwtConfigTest_AllValid() throws KeySourceException {

        String issue = "";
        List<String> oidcIssuersDiscoveryEndpoints = null;
        String publicKeyBase64 = "";
        String privateKeyBase64 = "";
        String keyId = "";
        long validityInSeconds = 0;
        String keyAlgorithm = "";
        String signingAlgorithm = "";

        JwtConfig jwtConfig = new JwtConfig(
                issue,
                oidcIssuersDiscoveryEndpoints,
                publicKeyBase64,
                privateKeyBase64,
                keyId,
                validityInSeconds,
                keyAlgorithm,
                signingAlgorithm
        );
        jwtConfig.getPrivateKey();
        jwtConfig.getPrivateKey();
        RSAKey jwtConfigRsaKey = jwtConfig.getRsaJwk();

        JwtKeys jwtKeys = new JwtKeys(jwtConfig);
        List<JWK> keys = jwtKeys.jwkSource().get(new JWKSelector(new JWKMatcher.Builder().keyID(keyId).build()), null);
        assertThat(keys).hasSize(1);
        assertThat(keys.get(0)).hasSameClassAs(RSAKey.class);
        RSAKey jwkSourceRsaKey = (RSAKey) keys.get(0);
        assertThat(jwkSourceRsaKey).isEqualTo(jwtConfigRsaKey);
    }

    @Test
    public void jwtConfigTest_InvalidPublicKey() {

        String issue = "";
        List<String> oidcIssuersDiscoveryEndpoints = null;
        String publicKeyBase64 = "";
        String privateKeyBase64 = "";
        String keyId = "";
        long validityInSeconds = 0;
        String keyAlgorithm = "";
        String signingAlgorithm = "";

        JwtConfig jwtConfig = new JwtConfig(
                issue,
                oidcIssuersDiscoveryEndpoints,
                publicKeyBase64,
                privateKeyBase64,
                keyId,
                validityInSeconds,
                keyAlgorithm,
                signingAlgorithm
        );
        jwtConfig.getPrivateKey();
        jwtConfig.getPrivateKey();
        jwtConfig.getRsaJwk();
    }

    @Test
    public void jwtConfigTest_InvalidPrivateKey() {

        String issue = "";
        List<String> oidcIssuersDiscoveryEndpoints = null;
        String publicKeyBase64 = "";
        String privateKeyBase64 = "";
        String keyId = "";
        long validityInSeconds = 0;
        String keyAlgorithm = "";
        String signingAlgorithm = "";

        JwtConfig jwtConfig = new JwtConfig(
                issue,
                oidcIssuersDiscoveryEndpoints,
                publicKeyBase64,
                privateKeyBase64,
                keyId,
                validityInSeconds,
                keyAlgorithm,
                signingAlgorithm
        );
        jwtConfig.getPrivateKey();
        jwtConfig.getPrivateKey();
        jwtConfig.getRsaJwk();
    }

    @Test
    public void jwtConfigTest_InvalidKeyAlgorithm() {

        String issue = "";
        List<String> oidcIssuersDiscoveryEndpoints = null;
        String publicKeyBase64 = "";
        String privateKeyBase64 = "";
        String keyId = "";
        long validityInSeconds = 0;
        String keyAlgorithm = "";
        String signingAlgorithm = "";

        JwtConfig jwtConfig = new JwtConfig(
                issue,
                oidcIssuersDiscoveryEndpoints,
                publicKeyBase64,
                privateKeyBase64,
                keyId,
                validityInSeconds,
                keyAlgorithm,
                signingAlgorithm
        );
        jwtConfig.getPrivateKey();
        jwtConfig.getPrivateKey();
        jwtConfig.getRsaJwk();
    }

    @Test
    public void jwtConfigTest_InvalidSigningAlgorithm() {

        String issue = "";
        List<String> oidcIssuersDiscoveryEndpoints = null;
        String publicKeyBase64 = "";
        String privateKeyBase64 = "";
        String keyId = "";
        long validityInSeconds = 0;
        String keyAlgorithm = "";
        String signingAlgorithm = "";

        JwtConfig jwtConfig = new JwtConfig(
                issue,
                oidcIssuersDiscoveryEndpoints,
                publicKeyBase64,
                privateKeyBase64,
                keyId,
                validityInSeconds,
                keyAlgorithm,
                signingAlgorithm
        );
        jwtConfig.getPrivateKey();
        jwtConfig.getPrivateKey();
        jwtConfig.getRsaJwk();
    }

    // createAccountAccessToken
    //  > Mock JWT Keys
    //  > Mock JWT Config

    // createClientAccessToken
}
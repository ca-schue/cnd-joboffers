package thi.cnd.authservice.adapters.out.security.jwt;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.account.Account;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.authservice.domain.model.account.InternalAccount;
import thi.cnd.authservice.domain.model.account.OidcAccount;
import thi.cnd.authservice.domain.model.client.Client;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {JwtProvider.class, JwtClaims.class, JwkSet.class})
public class JwtSliceTest {

    JwtProvider jwtProvider;
    JwtClaims jwtClaims;
    JwkSet jwkSet;
    JwtDecoder jwtDecoder;

    @Autowired
    public JwtSliceTest(JwtProvider jwtProvider, JwtClaims jwtClaims, JwkSet jwkSet) throws JOSEException {
        this.jwtProvider = jwtProvider;
        this.jwtClaims = jwtClaims;
        this.jwkSet = jwkSet;
        RSAPublicKey rsaPublicKey = extractRSAPublicKeyFromJWKSet();
        this.jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    @Test
    public void testInternalAccountJwt () {
        AccountId id = new AccountId();
        Instant lastLogin = Instant.now();
        String email = "some@mail";
        String encryptedPassword = Base64.getEncoder().encodeToString("S0meP@ssword".getBytes());
        boolean verified = true;
        Account account = new InternalAccount(id, lastLogin, email, encryptedPassword, verified);

        AccessToken accessToken = jwtProvider.createAccountAccessToken(account);
        Jwt decodedJwt = jwtDecoder.decode(accessToken.getTokenValue());
        Map<String, Object> claims = decodedJwt.getClaims();
        assertEquals(id.toString(), claims.get("sub"));
        assertEquals("account", claims.get("subject-type"));
    }

    @Test
    public void testOidcAccountJwt () {
        AccountId id = new AccountId();
        Instant lastLogin = Instant.now();
        String subject = "some-subject";
        boolean verified = true;
        Account account = new OidcAccount(id, lastLogin, subject, verified);

        AccessToken accessToken = jwtProvider.createAccountAccessToken(account);
        Jwt decodedJwt = jwtDecoder.decode(accessToken.getTokenValue());
        Map<String, Object> claims = decodedJwt.getClaims();
        assertEquals(id.toString(), claims.get("sub"));
        assertEquals("account", claims.get("subject-type"));
    }

    @Test
    public void testClientJwt () {
        String name = "testclient";
        String encryptedPassword = Base64.getEncoder().encodeToString("S0meP@ssword".getBytes());
        Set<String> audiences = Set.of("service1", "service2");
        Set<String> scopes = Set.of("canDoA", "canDoB");
        Instant lastLogin = Instant.now();
        Instant lastPasswordChange = Instant.now();
        Client client = new Client(name,encryptedPassword,audiences,scopes,lastLogin,lastPasswordChange);

        AccessToken accessToken = jwtProvider.createClientAccessToken(client);
        Jwt decodedJwt = jwtDecoder.decode(accessToken.getTokenValue());
        Map<String, Object> claims = decodedJwt.getClaims();
        assertEquals(name, claims.get("sub"));
        assertEquals("client", claims.get("subject-type"));
        assertArrayEquals(audiences.toArray(), ((ArrayList) claims.get("aud")).toArray());
        assertArrayEquals(scopes.toArray(), ((ArrayList) claims.get("scope")).toArray());
    }

    private RSAPublicKey extractRSAPublicKeyFromJWKSet() throws JOSEException {
        ImmutableJWKSet immutableJWKSet = (ImmutableJWKSet) this.jwkSet.getJwkSet();
        List<JWK> jwkList = immutableJWKSet.getJWKSet().getKeys();
        for (JWK jwk : jwkList) {
            if (jwk instanceof RSAKey) {
                return ((RSAKey) jwk).toRSAPublicKey();
            }
        }
        throw new IllegalArgumentException("RSAPublicKey not found in JWKSet");
    }
}

package thi.cnd.authservice.core.domain.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import sun.misc.Signal;
import thi.cnd.authservice.core.model.account.Account;
import thi.cnd.authservice.core.model.account.AccountAccessToken;
import thi.cnd.authservice.core.model.client.Client;
import thi.cnd.authservice.core.model.client.ClientAccessToken;

import java.time.Instant;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtProvider {


    private final JwtConfig jwtConfig;


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
                .subject(account.getId().id().toString())
                .build();
        return accountJwtClaims;
    }

    private JwtClaimsSet createClientClaims(Client client) {
        JwtClaimsSet clientJwtClaims = JwtClaimsSet.builder()
                .claim(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.CLIENT)
                .subject(client.name())
                .audience(client.audiences().stream().toList())
                .claim("scope", client.scopes().stream().toList())
                .build();
        return clientJwtClaims;
    }

    public Jwt generateSignedJwt(JwtClaimsSet specificJwtClaims) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtConfig.getValidityInSeconds() * 1000);

        RSAKey rsaKeyJwk = jwtConfig.getRsaJwk();
        JWKSet jwkSet = new JWKSet(rsaKeyJwk);
        ImmutableJWKSet jwkSource = new ImmutableJWKSet<>(jwkSet);
        NimbusJwtEncoder nimbusJwtEncoder = new NimbusJwtEncoder(jwkSource);

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
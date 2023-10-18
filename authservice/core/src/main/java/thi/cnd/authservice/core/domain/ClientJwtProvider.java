package thi.cnd.authservice.core.domain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.exceptions.ClientNotFoundByNameException;
import thi.cnd.authservice.core.model.Client;
import thi.cnd.authservice.core.ports.secondary.ClientRepositoryPort;


import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class ClientJwtProvider {

    private final ClientRepositoryPort port;
    private final JwtConfig jwtConfig;

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        var jwkSet = new JWKSet(
                new RSAKey.Builder((RSAPublicKey) jwtConfig.getPublicKey())
                        .privateKey(jwtConfig.getPrivateKey())
                        .keyID(jwtConfig.getKeyId())
                        .build()
        );
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> clientTokenCustomizer() {
        return (context -> {
            if (Objects.equals(context.getTokenType().getValue(), "access_token")
                    && context.getPrincipal() instanceof OAuth2ClientAuthenticationToken principal
                    && principal.getRegisteredClient() instanceof RegisteredClient registeredClient) {

                Client client = null;
                try {
                    // This is a work-around: We cannot save "audiences" in the RegisteredClient Object (OAuth2 design decision, https://github.com/spring-projects/spring-authorization-server/issues/634)
                    // Solution: Get client from repository again, only to retrieve audiences. This is an unnecessary 2nd call to the secondary adapters.
                    client = port.findByName(registeredClient.getId());
                } catch (ClientNotFoundByNameException e) {
                    throw new RuntimeException("Internal error: Could not find authenticated client " + client.name() + " in client repository"); // Error
                }
                context.getClaims()
                        .audience(client.audiences().stream().toList()) // Add audiences to the token
                        .claim(JwtConstants.SCOPE_TYPE_CLAIM_NAME, client.scopes().stream().collect(Collectors.joining(" ")))
                        .claim(JwtConstants.SUBJECT_TYPE_CLAIM_NAME, JwtConstants.CLIENT); // Add the 'client' type to the jwt token to identify client tokens
            }
        });
    }
}

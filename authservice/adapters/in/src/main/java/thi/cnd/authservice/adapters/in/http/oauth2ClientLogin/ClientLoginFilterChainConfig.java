package thi.cnd.authservice.adapters.in.http.oauth2ClientLogin;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.adapters.in.http.CorsConfig;
import thi.cnd.authservice.domain.ClientService;
import thi.cnd.authservice.domain.model.AccessToken;
import thi.cnd.authservice.domain.model.client.Client;

@Configuration
@AllArgsConstructor
class ClientLoginFilterChainConfig {


    private final CorsConfig corsConfig;
    private final String jwtIssuer;
    private final ClientService clientService;

    @Autowired
    public ClientLoginFilterChainConfig(
            @Value("${jwt-config.issuer}") String jwtIssuer,
            ClientService clientService,
            CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
        this.jwtIssuer = jwtIssuer;
        this.clientService = clientService;
    }

    private OAuth2TokenGenerator<Jwt> oauth2ClientLogin() {
        return (context) -> {
            OAuth2ClientAuthenticationToken oAuth2ClientAuthenticationToken = context.getPrincipal();
            CustomRegisteredClient customRegisteredClient = (CustomRegisteredClient) oAuth2ClientAuthenticationToken.getRegisteredClient();
            Client client = customRegisteredClient.getClient();
            AccessToken clientAccessToken = clientService.mintClientAccessToken(client);
            return new Jwt(
                    clientAccessToken.getTokenValue(),
                    clientAccessToken.getIssuedAt(),
                    clientAccessToken.getExpiresAt(),
                    clientAccessToken.getHeaders(),
                    clientAccessToken.getClaims()
            );
        };
    }

    @Bean
    @Order(2)
    public SecurityFilterChain authorizationServerTokenSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher("/oauth2/**");
        http.apply(authorizationServerConfigurer)
                .authorizationServerSettings(providerSettings())
                .tokenGenerator(this.oauth2ClientLogin());
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(this.jwtIssuer)
                .build();
    }

}

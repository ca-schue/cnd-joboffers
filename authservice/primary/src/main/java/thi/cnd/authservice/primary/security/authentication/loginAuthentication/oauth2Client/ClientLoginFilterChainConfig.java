package thi.cnd.authservice.primary.security.authentication.loginAuthentication.oauth2Client;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.core.domain.jwt.JwtConfig;
import thi.cnd.authservice.primary.rest.client.ClientController;
import thi.cnd.authservice.primary.security.cors.CorsConfig;

@Configuration
@AllArgsConstructor
public class ClientLoginFilterChainConfig {

    private final JwtConfig jwtConfig;
    private final ClientController clientController;
    private final CorsConfig corsConfig;

    /*
    // Auth server
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration
                .j
                .applyDefaultSecurity(http);
        return http.build();
    }*/


    @Bean
    @Order(2)
    public SecurityFilterChain authorizationServerTokenSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher("/oauth2/**");
        http.apply(authorizationServerConfigurer)
                .authorizationServerSettings(providerSettings())
                .tokenGenerator(clientController.loginClient());
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }


    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(jwtConfig.getIssuer())
                .build();
    }

}

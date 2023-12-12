package thi.cnd.authservice.adapters.in.security.authentication.loginAuthentication.oauth2Client;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.adapters.in.http.client.ClientHttpControllerImpl;
import thi.cnd.authservice.adapters.in.security.cors.CorsConfig;

@Configuration
@AllArgsConstructor
public class ClientLoginFilterChainConfig {


    private final ClientHttpControllerImpl clientHttpControllerImpl;
    private final CorsConfig corsConfig;

    private final String jwtIssuer;

    @Autowired
    public ClientLoginFilterChainConfig(
            @Value("${jwt-config.issuer}") String jwtIssuer,
            ClientHttpControllerImpl clientHttpControllerImpl,
            CorsConfig corsConfig) {
        this.clientHttpControllerImpl = clientHttpControllerImpl;
        this.corsConfig = corsConfig;
        this.jwtIssuer = jwtIssuer;
    }

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
                .tokenGenerator(clientHttpControllerImpl.loginClient());
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }


    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(this.jwtIssuer)
                .build();
    }

}

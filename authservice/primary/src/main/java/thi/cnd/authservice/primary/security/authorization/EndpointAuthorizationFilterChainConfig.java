package thi.cnd.authservice.primary.security.authorization;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AccessTokenAuthenticationConverter;
import thi.cnd.authservice.primary.security.authorization.authorizationManager.AuthenticationTypeAuthorizationManager;
import thi.cnd.authservice.primary.security.authorization.authorizationManager.IdMatcherAuthorizationManager;
import thi.cnd.authservice.primary.security.cors.CorsConfig;

import java.util.List;

@Validated
@Configuration
@AllArgsConstructor
public class EndpointAuthorizationFilterChainConfig {

    private final CorsConfig corsConfig;

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers("/accounts/registerInternalAccount").permitAll()
                )
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.PUT,
                                "/accounts/{accountId}/update-internal-password",
                                "/accounts/{accountId}/update-internal-password")
                            .access(AuthorizationManagers.allOf(
                                    AuthenticationTypeAuthorizationManager.isAccount(),
                                    IdMatcherAuthorizationManager.matchesId("accountId")))
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.DELETE,"/accounts/{accountId}")
                            .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccount(),
                                IdMatcherAuthorizationManager.matchesId("accountId")))
                )
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/clients/create").permitAll()
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.PUT,"/clients/{clientId}/reset-password")
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isClient(),
                                IdMatcherAuthorizationManager.matchesId("clientId")))
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.GET,"/token-introspection/account")
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccount()))
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.GET,"/token-introspection/client")
                        .access(AuthorizationManagers.allOf(
                                        AuthenticationTypeAuthorizationManager.isClient()))
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes access tokens issued by this authorization server
                        .jwt(jwtConfig -> jwtConfig
                                //.jwkSetUri("http://localhost:8081")
                                .jwtAuthenticationConverter(new AccessTokenAuthenticationConverter())));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }

    /*
    @Bean
    @ConditionalOnProperty(value = "security.corsActive", havingValue = "false")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("PUT", "DELETE", "GET", "POST");
            }
        };
    }*/

}

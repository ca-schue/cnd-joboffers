package thi.cnd.authservice.primary.security.authorization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AccessTokenAuthenticationConverter;
import thi.cnd.authservice.primary.security.authorization.authorizationManager.AuthenticationTypeAuthorizationManager;
import thi.cnd.authservice.primary.security.authorization.authorizationManager.IdMatcherAuthorizationManager;

@Validated
@Configuration
public class EndpointAuthorizationFilterChainConfig {


    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/accounts/registerInternalAccount").permitAll()
                        .requestMatchers("/clients/create").permitAll()
                        .requestMatchers("/accounts/{accountId}").access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccount(),
                                IdMatcherAuthorizationManager.matchesId("accountId")))
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes access tokens issued by this authorization server
                        .jwt(jwtConfig -> jwtConfig
                                .jwtAuthenticationConverter(new AccessTokenAuthenticationConverter())));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

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
    }

}

package thi.cnd.userservice.primary.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import thi.cnd.userservice.primary.security.authorizationManagers.IdMatcherAuthorizationManager;
import thi.cnd.userservice.primary.security.tokenProcessing.AccessTokenAuthenticationConverter;

@Configuration
public class EndpointAuthorizationFilterConfig {

    @Bean
    SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http
                //.securityMatcher("/token-introspection", "/clients/{clientId}/reset-password", "/accounts/{accountId}")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/**").permitAll()
                        //.access(AuthorizationManagers.allOf(
                        //        AuthorityAuthorizationManager.hasRole(SecurityConstants.ACCOUNT)))
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

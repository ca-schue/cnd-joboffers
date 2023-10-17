package thi.cnd.authservice.primary.rest.security;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import thi.cnd.authservice.core.domain.JwtConfig;
import thi.cnd.authservice.core.domain.JwtConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class FilterChains {

    private final JwtConfig jwtConfig;

    private final boolean csrfActive = false;
    private final boolean corsActive = false;

    @Autowired
    public FilterChains(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    @Order(1)
    SecurityFilterChain internalAccountLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                    //.requestMatchers("accounts/loginInternalAccount").permitAll()
                    .requestMatchers("/accounts/registerInternalAccount").permitAll()
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
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


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Handles Client registration + login
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(jwtConfig.getIssuer())
                .build();
    }
    /*

    @Bean
    @Order(1)
    SecurityFilterChain internalAccountLoginFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        //.requestMatchers("accounts/loginInternalAccount").permitAll()
                        .anyRequest().permitAll());
                //.authenticationProvider(new DaoAuthenticationProvider())
                //.httpBasic(Customizer.withDefaults()); // Only allows Basic Authentication
        return http.build();
    }



    @Bean
    @Order(2)
    SecurityFilterChain oidcAccountLoginFilterChain(HttpSecurity http) throws Exception {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
                new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
        List<String> issuers = jwtConfig.getOidcIssuers();
        issuers.stream().forEach(issuer -> addManager(authenticationManagers, issuer));

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("accounts/loginOIDCAccount").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes ID-Tokens issued by supported OIDC providers (internal access tokens are denied)
                        .authenticationManagerResolver(authenticationManagerResolver));
        return http.build();
    }

    public void addManager(Map<String, AuthenticationManager> authenticationManagers, String issuer) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(JwtDecoders.fromOidcIssuerLocation(issuer));
        authenticationProvider.setJwtAuthenticationConverter(getJwtAuthenticationConverter());
        authenticationManagers.put(issuer, authenticationProvider::authenticate);
    }

    private Converter<Jwt, AuthenticatedOidcIdToken> getJwtAuthenticationConverter() {
        OidcAuthenticationConverter conv = new OidcAuthenticationConverter();
        return conv;
    }

    class OidcAuthenticationConverter implements Converter<Jwt, AuthenticatedOidcIdToken> {
        public AuthenticatedOidcIdToken convert(Jwt jwt) {
            if(!jwt.hasClaim("email")) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "Email Claim missing in JWT", null);
                throw new OAuth2AuthenticationException(error);
            } else if(jwt.getIssuer().toString().equals(jwtConfig.getIssuer())) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, "JWT was issued from this server. Endpoint only callable with JWT issued by supported OIDC providers", null);
                throw new OAuth2AuthenticationException(error);
            } else {
                String email = jwt.getClaimAsString("email");
                AuthenticatedOidcIdToken authenticatedOidcToken = new AuthenticatedOidcIdToken(email);
                return authenticatedOidcToken;
            }
        }
    }

    @Bean
    @Order(3)
    SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .requestMatchers("/token-introspection").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/accounts/{accountId}").authenticated()
                .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes access tokens issued by this authorization server
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new AccessTokenAuthenticationConverter())));
        return http.build();
    }

    class AccessTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        @Override
        public AbstractAuthenticationToken convert(Jwt source) {
            String subjectType = source.getClaimAsString(JwtConstants.SUBJECT_TYPE_CLAIM_NAME);
            if (subjectType.equals(JwtConstants.USER)) {
                return new AuthenticatedAccount(source);
            } else if (subjectType.equals(JwtConstants.CLIENT)) {
                return new AuthenticatedClient(source);
            } else {
                throw new RuntimeException("Unsupported subject type " + subjectType);
            }
        }
    }*/

}

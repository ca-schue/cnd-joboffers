package thi.cnd.authservice.primary.rest.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.core.domain.JwtConfig;
import thi.cnd.authservice.core.domain.JwtConstants;

//@Configuration
//@EnableWebSecurity
//public class FilterChains {


    //private final JwtConfig jwtConfig;

    /*@Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.anyRequest().permitAll()).build();
    }

    @Bean
    public AuthorizationServerSettings providerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(jwtConfig.getIssuer())
                .build();
    }*/

    /*@Bean
    @Order(1)
    SecurityFilterChain internalAccountLoginFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("accounts/loginInternalAccount").authenticated()
                        .anyRequest().permitAll())
                .authenticationProvider(new DaoAuthenticationProvider())
                .httpBasic(Customizer.withDefaults()); // Only allows Basic Authentication
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain oidcAccountLoginFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("accounts/loginOIDCAccount").authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes ID-Tokens issued by supported OIDC providers (internal access tokens are denied)
                        .authenticationManagerResolver(new JwtIssuerAuthenticationManagerResolver(jwtConfig.getOidcIssuers()))
                                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(new OidcAuthenticationConverter()))
                );
        return http.build();
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
                .requestMatchers("/accounts/registerInternalAccount").permitAll()
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

//}

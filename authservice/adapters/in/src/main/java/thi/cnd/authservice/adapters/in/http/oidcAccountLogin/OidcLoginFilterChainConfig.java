package thi.cnd.authservice.adapters.in.http.oidcAccountLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.adapters.in.http.CorsConfig;

import java.util.HashMap;
import java.util.Map;

@Configuration
class OidcLoginFilterChainConfig {

    private final AuthenticatedOidcIdTokenService authenticatedOidcIdTokenService;
    private final CorsConfig corsConfig;

    private final String oidcIssuerDiscoveryEndpoint;

    private final String jwtIssuer;


    @Autowired
    public OidcLoginFilterChainConfig(
        AuthenticatedOidcIdTokenService authenticatedOidcIdTokenService,
        CorsConfig corsConfig,
        @Value("${oidc.oidcProviderDiscoveryEndpoint}") String oidcIssuerDiscoveryEndpoint,
        @Value("${jwt-config.issuer}") String jwtIssuer

    ) {
        this.authenticatedOidcIdTokenService = authenticatedOidcIdTokenService;
        this.corsConfig = corsConfig;
        this.oidcIssuerDiscoveryEndpoint = oidcIssuerDiscoveryEndpoint;
        this.jwtIssuer = jwtIssuer;
    }


    // OIDC
    @Bean
    @Order(3)
    SecurityFilterChain oidcAccountLoginFilterChain(HttpSecurity http) throws Exception {
        Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();
        JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
                new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);
        addManager(authenticationManagers, oidcIssuerDiscoveryEndpoint);

        http
                .securityMatcher("/accounts/loginOIDCAccount")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes ID-Tokens issued by supported OIDC providers (internal access tokens are denied)
                        .authenticationManagerResolver(authenticationManagerResolver));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }

    public void addManager(Map<String, AuthenticationManager> authenticationManagers, String oidcIssuersDiscoveryEndpoint) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(JwtDecoders.fromOidcIssuerLocation(oidcIssuersDiscoveryEndpoint));
        authenticationProvider.setJwtAuthenticationConverter(getJwtAuthenticationConverter());
        authenticationManagers.put(oidcIssuersDiscoveryEndpoint, authenticationProvider::authenticate);
    }

    private Converter<Jwt, AuthenticatedOidcIdToken> getJwtAuthenticationConverter() {
        OidcAuthenticationConverter conv = new OidcAuthenticationConverter();
        return conv;
    }

    class OidcAuthenticationConverter implements Converter<Jwt, AuthenticatedOidcIdToken> {
        public AuthenticatedOidcIdToken convert(Jwt jwt) {
            String subject = jwt.getSubject();
            if(subject.isEmpty()) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "Subject Claim missing in JWT", null);
                throw new OAuth2AuthenticationException(error);
            } else if(jwt.getIssuer().toString().equals(jwtIssuer)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED, "JWT was issued from this server. Only external OIDC providers supported for this endpoint. Use /account/loginInternalAccount instead.", null);
                throw new OAuth2AuthenticationException(error);
            } else {
                AuthenticatedOidcIdToken authenticatedOidcToken = authenticatedOidcIdTokenService.loadIdTokenBySubject(subject);
                return authenticatedOidcToken;
            }
        }
    }



}

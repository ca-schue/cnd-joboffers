package thi.cnd.authservice.primary.rest.security.loginAuthentication;

public class OidcLoginFilterChainConfig {


    /*
    // OIDC
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

*/

}

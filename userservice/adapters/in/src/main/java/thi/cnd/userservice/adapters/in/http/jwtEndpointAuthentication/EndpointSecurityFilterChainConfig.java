package thi.cnd.userservice.adapters.in.http.jwtEndpointAuthentication;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class EndpointSecurityFilterChainConfig {

    private final CorsConfig corsConfig;

    @Bean
    SecurityFilterChain authorizationFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers("/actuator/**").permitAll()
                )
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.PUT, "/users/register")
                        // unauthorized
                        .permitAll()
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/users/{user-id}")
                        // Client or Verified Account with same ID
                        .access(AuthorizationManagers.anyOf(
                                AuthorizationManagers.allOf(
                                        AuthenticationTypeAuthorizationManager.isClient(),
                                        AuthorityAuthorizationManager.hasAuthority("SCOPE_getUser")
                                ),
                                AuthorizationManagers.allOf(
                                        AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                        IdMatcherAuthorizationManager.matchesId("user-id")
                                )
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.DELETE,"/users/{user-id}")
                        // Verified Account with same ID
                        .access(
                                AuthorizationManagers.allOf(
                                        AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                        IdMatcherAuthorizationManager.matchesId("user-id")
                                )
                        )
                )
                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/users/{user-id}/public-profile")
                        // Client or verified Account
                        .access(AuthorizationManagers.anyOf(
                                AuthenticationTypeAuthorizationManager.isClient(),
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified()
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/users/{user-id}/update-user-profile")
                        // Verified Account with same ID
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                IdMatcherAuthorizationManager.matchesId("user-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/users/{user-id}/update-user-settings")
                        // Verified Account with same ID
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                IdMatcherAuthorizationManager.matchesId("user-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/users/{user-id}/subscribe")
                        // Verified Account with same ID
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                IdMatcherAuthorizationManager.matchesId("user-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/users/{user-id}/accept-company-invitation")
                        // Verified Account with same ID
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                IdMatcherAuthorizationManager.matchesId("user-id")
                        ))
                )


                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/companies")
                        // Unauthorized
                        .permitAll()
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.PUT,"/companies/register")
                        // Verified Account
                        .access(AuthenticationTypeAuthorizationManager.isAccountAndVerified())
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/companies/{company-id}")
                        // (Client && SCOPE_getCompany) || (Verified Account && ( Member || Invited ))
                        .access(
                            AuthorizationManagers.anyOf(
                                AuthorizationManagers.allOf(
                                    AuthenticationTypeAuthorizationManager.isClient(),
                                    AuthorityAuthorizationManager.hasAuthority("SCOPE_getCompany")
                                ),
                                AuthorizationManagers.allOf(
                                    AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                    AuthorizationManagers.anyOf(
                                        CompanyMemberAuthorizationManager.isMember("company-id"),
                                        UserInvitedAuthorizationManager.isInvited("company-id")
                                    )
                                )
                            )
                        )
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.DELETE,"/companies/{company-id}")
                        // Verified Account && Owner
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                CompanyOwnerAuthorizationManager.isOwner("company-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/companies/{company-id}/public-profile")
                        // Unauthorized
                        .permitAll()
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/companies/{company-id}/invite-user")
                        // Verified Account && Member
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                CompanyMemberAuthorizationManager.isMember("company-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/companies/{company-id}/partner-program")
                        // Verified Account && Owner
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                CompanyOwnerAuthorizationManager.isOwner("company-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/companies/{company-id}/update-company-details")
                        // Verified Account && Owner
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                CompanyOwnerAuthorizationManager.isOwner("company-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.POST,"/companies/{company-id}/update-company-links")
                        // Verified Account && Owner
                        .access(AuthorizationManagers.allOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                CompanyOwnerAuthorizationManager.isOwner("company-id")
                        ))
                )

                .authorizeHttpRequests(authorization -> authorization
                        .requestMatchers(HttpMethod.GET,"/token-introspection")
                        // Verified Account || Client
                        .access(AuthorizationManagers.anyOf(
                                AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
                                AuthenticationTypeAuthorizationManager.isClient()
                        ))
                )

                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2 // Only takes access tokens issued by this authorization server
                        .jwt(jwtConfig -> jwtConfig
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

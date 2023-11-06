package thi.cnd.careerservice.http.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import thi.cnd.careerservice.http.security.authentication.AccessTokenAuthenticationConverter;
import thi.cnd.careerservice.http.security.authorization.AuthenticationTypeAuthorizationManager;
import thi.cnd.careerservice.http.security.authorization.CompanyMemberAuthorizationManager;
import thi.cnd.careerservice.http.security.authorization.IdMatcherAuthorizationManager;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Security configuration for all http endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final boolean csrfActive;
    private final boolean corsActive;

    public SecurityFilterChainConfig(
        @Value("${security.csrfActive}") boolean csrfActive,
        @Value("${security.corsActive}") boolean corsActive) {
        this.csrfActive = csrfActive;
        this.corsActive = corsActive;
    }

    @Bean
    @Profile("!integrationtest")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/available-job-offers").permitAll()
                .requestMatchers(GET, "/companies/{company-id}/available-job-offers").permitAll()
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{job-offer-id}").permitAll()
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(POST, "/companies/{company-id}/job-offers")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(DELETE, "/companies/{company-id}/job-offers/{job-offer-id}")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{job-offer-id}/update")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{job-offer-id}/publish")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{job-offer-id}/close")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{job-offer-id}/job-applications")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}")
                .access(verifyUserIsMemberOfCompany())
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}/close")
                .access(verifyUserIsMemberOfCompany())
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/users/{user-id}/job-applications")
                .access(verifyUserHasSameId())
                .requestMatchers(POST, "/users/{user-id}/job-offers/{job-offer-id}/create-job-applications")
                .access(verifyUserHasSameId())
                .requestMatchers(GET, "/users/{user-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}")
                .access(verifyUserHasSameId())
                .requestMatchers(DELETE, "/users/{user-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}")
                .access(verifyUserHasSameId())
                .requestMatchers(POST, "/users/{user-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}/update")
                .access(verifyUserHasSameId())
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/actuator/**").permitAll()
            )
            //.authorizeHttpRequests(request -> request.anyRequest().authenticated())
            .authorizeHttpRequests(request -> request.anyRequest().permitAll())
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2ResourceServer(oauth2 -> oauth2 // Only takes access tokens issued by this authorization server
                .jwt(jwtConfig -> jwtConfig
                    .jwtAuthenticationConverter(new AccessTokenAuthenticationConverter())));;

        http.csrf(csrf -> setActive(csrf, csrfActive));

        return http.cors(Customizer.withDefaults()).build();
    }

    private AuthorizationManager<RequestAuthorizationContext> verifyUserIsMemberOfCompany() {
        return AuthorizationManagers.allOf(
            AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
            CompanyMemberAuthorizationManager.isMember("company-id")
        );
    }

    private AuthorizationManager<RequestAuthorizationContext> verifyUserHasSameId() {
        return AuthorizationManagers.allOf(
            AuthenticationTypeAuthorizationManager.isAccountAndVerified(),
            IdMatcherAuthorizationManager.matchesId("user-id")
        );
    }


    private void setActive(AbstractHttpConfigurer<?, HttpSecurity> configurer, boolean isActive) {
        if (!isActive) {
            configurer.disable();
        }
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
                    .allowedMethods("*");
            }
        };
    }

}

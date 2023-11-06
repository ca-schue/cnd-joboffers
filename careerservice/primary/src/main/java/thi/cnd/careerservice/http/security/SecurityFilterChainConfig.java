package thi.cnd.careerservice.http.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import thi.cnd.careerservice.http.security.authentication.AccessTokenAuthenticationConverter;

import static org.springframework.http.HttpMethod.GET;

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
            /*
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/job-offers").permitAll()
                .requestMatchers(GET, "/companies/{company-id}/job-offers").permitAll()
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{jobOfferId}").permitAll()
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(POST, "/companies/{company-id}/job-offers")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(DELETE, "/companies/{company-id}/job-offers/{jobOfferId}")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{jobOfferId}/update")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{jobOfferId}/publish")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{jobOfferId}/close")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{jobOfferId}/job-applications")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(GET, "/companies/{company-id}/job-offers/{jobOfferId}/job-applications/{jobApplicationId}")
                .access(av::verifyUserIsMemberOfCompany)
                .requestMatchers(POST, "/companies/{company-id}/job-offers/{jobOfferId}/job-applications/{jobApplicationId}/close")
                .access(av::verifyUserIsMemberOfCompany)
            )
            .authorizeHttpRequests(request -> request
                .requestMatchers(GET, "/users/{user-id}/job-applications")
                .access(av::verifyUserHasSameId)
                .requestMatchers(POST, "/users/{user-id}/job-offers/{jobOfferId}/create-job-applications")
                .access(av::verifyUserHasSameId)
                .requestMatchers(GET, "/users/{user-id}/job-offers/{jobOfferId}/job-applications/{jobApplicationId}")
                .access(av::verifyUserHasSameId)
                .requestMatchers(DELETE, "/users/{user-id}/job-offers/{jobOfferId}/job-applications/{jobApplicationId}")
                .access(av::verifyUserHasSameId)
                .requestMatchers(POST, "/users/{user-id}/job-offers/{jobOfferId}/job-applications/{jobApplicationId}/update")
                .access(av::verifyUserHasSameId)
            )*/

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

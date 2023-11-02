package thi.cnd.authservice.primary.security.authentication.loginAuthentication.internalAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import thi.cnd.authservice.core.domain.password.PasswordEncoder;
import thi.cnd.authservice.primary.security.cors.CorsConfig;

import java.util.List;

@Configuration
public class InternalAccountLoginFilterChainConfig {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final CorsConfig corsConfig;

    @Autowired
    public InternalAccountLoginFilterChainConfig(InternalAccountDetailsService accountDetailsService, PasswordEncoder passwordEncoder, CorsConfig corsConfig) {
        this.daoAuthenticationProvider = new DaoAuthenticationProvider();
        this.daoAuthenticationProvider.setUserDetailsService(accountDetailsService);
        this.daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        this.corsConfig = corsConfig;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain internalAccountLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/accounts/loginInternalAccount")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .anyRequest().authenticated()
                )
                .authenticationProvider(this.daoAuthenticationProvider)
                .httpBasic(Customizer.withDefaults());
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource()));
        return http.build();
    }


}

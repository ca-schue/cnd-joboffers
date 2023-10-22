package thi.cnd.authservice.primary.security.authentication.loginAuthentication.internalAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import thi.cnd.authservice.core.domain.password.PasswordEncoder;

@Configuration
public class InternalAccountLoginFilterChainConfig {

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public InternalAccountLoginFilterChainConfig(InternalAccountDetailsService accountDetailsService, PasswordEncoder passwordEncoder) {
        this.daoAuthenticationProvider = new DaoAuthenticationProvider();
        this.daoAuthenticationProvider.setUserDetailsService(accountDetailsService);
        this.daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    }

    @Bean
    @Order(0)
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
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

}

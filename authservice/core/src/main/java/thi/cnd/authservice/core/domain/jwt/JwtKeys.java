package thi.cnd.authservice.core.domain.jwt;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class JwtKeys {

    private final JwtConfig jwtConfig;

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKeyJwk = jwtConfig.getRsaJwk();
        JWKSet jwkSet = new JWKSet(rsaKeyJwk);
        JWKSource jwkSource = new ImmutableJWKSet<>(jwkSet);
        return jwkSource;
    }
}

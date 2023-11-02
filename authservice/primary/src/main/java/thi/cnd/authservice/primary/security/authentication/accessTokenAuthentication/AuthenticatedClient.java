package thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.core.domain.jwt.JwtConstants;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class AuthenticatedClient extends AbstractAuthenticationToken {

    private final String clientName;

    public AuthenticatedClient(Jwt jwt) {
        super(Stream.concat(
                Stream.of(
                        jwt.getClaimAsString("scope").split(" ")
                        )
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                ,
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_" + JwtConstants.CLIENT)
                )
        ).collect(Collectors.toSet()));
        this.clientName = jwt.getClaimAsString(JwtConstants.SUBJECT_CLAIM_NAME);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}

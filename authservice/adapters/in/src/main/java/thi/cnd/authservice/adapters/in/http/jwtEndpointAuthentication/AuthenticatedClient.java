package thi.cnd.authservice.adapters.in.http.jwtEndpointAuthentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
class AuthenticatedClient extends AbstractAuthenticationToken {

    private final String clientName;

    public AuthenticatedClient(Jwt jwt) {
        super(Stream.concat(
                Stream.of(
                        jwt.getClaimAsString("scope").split(" ")
                        )
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                ,
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_" + JwtClaims.subjectTypeClient)
                )
        ).collect(Collectors.toSet()));
        this.clientName = jwt.getClaimAsString(JwtClaims.subjectClaimName);
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

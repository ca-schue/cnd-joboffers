package thi.cnd.userservice.primary.security.authentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.userservice.primary.security.SecurityConstants;

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
                        new SimpleGrantedAuthority("ROLE_" + SecurityConstants.CLIENT)
                )
        ).collect(Collectors.toSet()));
        this.clientName = jwt.getClaimAsString("sub");
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

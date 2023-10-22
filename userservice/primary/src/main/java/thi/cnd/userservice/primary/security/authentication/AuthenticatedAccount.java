package thi.cnd.userservice.primary.security.authentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.userservice.primary.security.SecurityConstants;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class AuthenticatedAccount extends AbstractAuthenticationToken {

    private final UUID accountId;

    public AuthenticatedAccount(Jwt jwt) {
        super(//Stream.concat(
                /*Stream.of(
                        jwt.getClaimAsString("scope").split(" ")
                        )
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                , */ // TODO: Concat with scopes, when implemented for accounts
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_" + SecurityConstants.ACCOUNT)
                        )
                .collect(Collectors.toSet()));
        this.accountId = UUID.fromString(jwt.getClaim("sub"));
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

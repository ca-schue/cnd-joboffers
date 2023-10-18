package thi.cnd.authservice.primary.rest.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import thi.cnd.authservice.core.domain.JwtConstants;
import thi.cnd.authservice.core.model.AccountId;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class AuthenticatedAccount extends AbstractAuthenticationToken {

    private final AccountId accountId;


    public AuthenticatedAccount(Jwt jwt) {
        super(//Stream.concat(
                /*Stream.of(
                        jwt.getClaimAsString("scope").split(" ")
                        )
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                , */ // TODO: Concat with scopes, when implemented for accounts
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_" + JwtConstants.CLIENT)
                        )
                .collect(Collectors.toSet()));
        this.accountId = AccountId.of(jwt.getClaim("sub"));
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

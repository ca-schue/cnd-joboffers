package thi.cnd.careerservice.http.security.authentication;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.Getter;
import thi.cnd.careerservice.http.security.SecurityConstants;

@Getter
public class AuthenticatedAccount extends AbstractAuthenticationToken {

    private final UUID accountId;
    private final boolean isVerified;

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
        this.accountId = UUID.fromString(jwt.getClaim(SecurityConstants.SUBJECT_CLAIM_NAME));
        this.isVerified = jwt.getClaim(SecurityConstants.VERIFIED_CLAIM_NAME);
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

package thi.cnd.authservice.adapters.in.http.jwtEndpointAuthentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import thi.cnd.authservice.domain.model.account.AccountId;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
class AuthenticatedAccount extends AbstractAuthenticationToken {

    private final AccountId accountId;
    private final boolean isVerified;

    public AuthenticatedAccount(Jwt jwt) {
        super(//Stream.concat(
                /*Stream.of(
                        jwt.getClaimAsString("scope").split(" ")
                        )
                        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                , */ // TODO: Concat with scopes, when implemented for accounts
                Stream.of(
                        new SimpleGrantedAuthority("ROLE_" + JwtClaims.subjectTypeAccount)
                        )
                .collect(Collectors.toSet()));
        this.accountId = AccountId.of(jwt.getClaim(JwtClaims.subjectClaimName).toString());
        this.isVerified = jwt.getClaim(JwtClaims.verifiedClaimName);
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

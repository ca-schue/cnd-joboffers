package thi.cnd.authservice.primary.rest.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class AuthenticatedOidcIdToken extends AbstractAuthenticationToken {

    private final String email;

    public AuthenticatedOidcIdToken(String email) {
        super(null);
        this.setAuthenticated(true);
        this.email = email;
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

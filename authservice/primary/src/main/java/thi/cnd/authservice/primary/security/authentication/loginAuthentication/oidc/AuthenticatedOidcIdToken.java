package thi.cnd.authservice.primary.security.authentication.loginAuthentication.oidc;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class AuthenticatedOidcIdToken extends AbstractAuthenticationToken {

    private final String email;
    private final String subject;

    public AuthenticatedOidcIdToken(String subject, String email) {
        super(null);
        this.setAuthenticated(true);
        this.subject = subject;
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

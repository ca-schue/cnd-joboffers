package thi.cnd.authservice.primary.security.authentication.loginAuthentication.oidc;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.ietf.jgss.Oid;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import thi.cnd.authservice.core.model.account.OidcAccount;

@Getter
@Validated
public class AuthenticatedOidcIdToken extends AbstractAuthenticationToken {

    private final String subject;
    private final OidcAccount oidcAccount;

    public AuthenticatedOidcIdToken(@NotNull String subject, @Nullable OidcAccount oidcAccount) {
        super(null);
        this.setAuthenticated(true);
        this.subject = subject;
        this.oidcAccount = oidcAccount;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return oidcAccount;
    }
}

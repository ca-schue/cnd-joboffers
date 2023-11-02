package thi.cnd.authservice.primary.security.authorization.authorizationManager;

import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AuthenticatedAccount;
import thi.cnd.authservice.primary.security.authentication.accessTokenAuthentication.AuthenticatedClient;

import java.util.function.Supplier;

@AllArgsConstructor
public class AuthenticationTypeAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    Class<? extends Authentication> classTypeToCheck;

    @Override
    public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext object) {
        Authentication authType = SecurityContextHolder.getContext().getAuthentication();
        return new AuthorizationDecision(classTypeToCheck.isInstance(authType));
    }

    public static AuthenticationTypeAuthorizationManager isAccount() {
        return new AuthenticationTypeAuthorizationManager(AuthenticatedAccount.class);
    }

    public static AuthenticationTypeAuthorizationManager isClient() {
        return new AuthenticationTypeAuthorizationManager(AuthenticatedClient.class);
    }
}

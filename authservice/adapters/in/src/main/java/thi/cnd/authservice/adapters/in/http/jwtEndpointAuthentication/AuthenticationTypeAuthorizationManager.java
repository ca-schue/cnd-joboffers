package thi.cnd.authservice.adapters.in.http.jwtEndpointAuthentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@AllArgsConstructor
class AuthenticationTypeAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    Class<? extends Authentication> classTypeToCheck;

    @Override
    public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext object) {
        Authentication authType = SecurityContextHolder.getContext().getAuthentication();
        return new AuthorizationDecision(classTypeToCheck.isInstance(authType));
    }

    public static AuthorizationManager<RequestAuthorizationContext> isAccount() {
        return AuthorityAuthorizationManager.hasRole(JwtClaims.subjectTypeAccount);
        //return new AuthenticationTypeAuthorizationManager(AuthenticatedAccount.class);
    }

    public static AuthorizationManager<RequestAuthorizationContext> isAccountVerified() {
        Authentication authType = SecurityContextHolder.getContext().getAuthentication();
        return (authentication, object) -> new AuthorizationDecision((authType instanceof AuthenticatedAccount authenticatedAccount) && authenticatedAccount.isVerified());
    }

    public static AuthorizationManager<RequestAuthorizationContext> isClient() {
        return AuthorityAuthorizationManager.hasRole(JwtClaims.subjectTypeClient);
        //return new AuthenticationTypeAuthorizationManager(AuthenticatedAccount.class);
    }
}

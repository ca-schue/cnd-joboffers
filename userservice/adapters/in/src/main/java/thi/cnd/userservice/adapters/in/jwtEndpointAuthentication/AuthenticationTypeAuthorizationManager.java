package thi.cnd.userservice.adapters.in.jwtEndpointAuthentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@AllArgsConstructor
public class AuthenticationTypeAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

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

    public static AuthorizationManager<RequestAuthorizationContext> isAccountAndVerified() {
        return (authentication, object) -> {
            Authentication authType = SecurityContextHolder.getContext().getAuthentication();
            return new AuthorizationDecision(
                    (authType instanceof AuthenticatedAccount authenticatedAccount) &&
                            (authenticatedAccount.isVerified()));
        };
    }

    public static AuthorizationManager<RequestAuthorizationContext> isClient() {
        System.out.println("isClient");
        return AuthorityAuthorizationManager.hasRole(JwtClaims.subjectTypeClient);
        //return new AuthenticationTypeAuthorizationManager(AuthenticatedAccount.class);
    }
}

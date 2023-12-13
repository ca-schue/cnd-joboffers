package thi.cnd.userservice.adapters.in.jwtEndpointAuthentication;

import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@AllArgsConstructor
public class IdMatcherAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private String idToCheck;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = (Authentication) authentication.get();
        String requestId = ((RequestAuthorizationContext) object).getVariables().get(this.idToCheck);
        String authId;
        switch (auth) {
            case AuthenticatedClient authClient -> authId = authClient.getClientName();
            case AuthenticatedAccount authAcc -> authId = authAcc.getAccountId().toString();
            default -> throw new IllegalStateException("Unexpected value: " + (Authentication) authentication); // TODO:!
        }
        return new AuthorizationDecision(requestId.matches(authId));
    }

    public static AuthorizationManager matchesId(String idToCheck) {
        return new IdMatcherAuthorizationManager(idToCheck);
    }


}

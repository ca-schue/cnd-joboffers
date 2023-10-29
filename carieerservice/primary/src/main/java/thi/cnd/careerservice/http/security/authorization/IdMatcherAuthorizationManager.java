package thi.cnd.careerservice.http.security.authorization;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import lombok.AllArgsConstructor;
import thi.cnd.careerservice.http.security.authentication.AuthenticatedAccount;
import thi.cnd.careerservice.http.security.authentication.AuthenticatedClient;

@AllArgsConstructor
public class IdMatcherAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private String idToCheck;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        String requestId = object.getVariables().get(this.idToCheck);
        String authId;
        switch (auth) {
            case AuthenticatedClient authClient -> authId = authClient.getClientName();
            case AuthenticatedAccount authAcc -> authId = authAcc.getAccountId().toString();
            default -> throw new IllegalStateException("Unexpected value: " + authentication);
        }
        return new AuthorizationDecision(requestId.matches(authId));
    }

    public static AuthorizationManager matchesId(String idToCheck) {
        return new IdMatcherAuthorizationManager(idToCheck);
    }


}

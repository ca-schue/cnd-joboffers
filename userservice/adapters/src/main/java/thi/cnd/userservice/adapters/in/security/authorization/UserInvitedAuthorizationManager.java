package thi.cnd.userservice.adapters.in.security.authorization;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.adapters.in.security.authentication.AuthenticatedAccount;
import thi.cnd.userservice.domain.exceptions.UserNotFoundByIdException;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserId;
import thi.cnd.userservice.domain.UserService;

import java.util.function.Supplier;

@Component
public class UserInvitedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Setter
    private String companyIdParameterName = "";
    private static UserService userService;

    @Autowired
    public void setCompanyServicePort(UserService userService) {
        UserInvitedAuthorizationManager.userService = userService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = (Authentication) authentication.get();
        String companyId = ((RequestAuthorizationContext) object).getVariables().get(this.companyIdParameterName);

        boolean decision = false;
        try {
            if((auth instanceof AuthenticatedAccount authenticatedAccount)) {
                User user = UserInvitedAuthorizationManager.userService.findUserById(UserId.of(authenticatedAccount.getAccountId().toString()));
                if (user.getAssociations().getInvitedTo().contains(CompanyId.of(companyId))) {
                    decision = true;
                }
            }
        } catch (UserNotFoundByIdException ignored) {
            decision = false;
        }
        return new AuthorizationDecision(decision);
    }

    public static UserInvitedAuthorizationManager isInvited(String companyIdParameterName) {
        UserInvitedAuthorizationManager companyMemberAuthorizationManager = new UserInvitedAuthorizationManager();
        companyMemberAuthorizationManager.setCompanyIdParameterName(companyIdParameterName);
        return companyMemberAuthorizationManager;
    }


}

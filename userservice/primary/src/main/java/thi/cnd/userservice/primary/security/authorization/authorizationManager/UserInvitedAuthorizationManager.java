package thi.cnd.userservice.primary.security.authorization.authorizationManager;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.exception.UserNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.port.primary.CompanyServicePort;
import thi.cnd.userservice.core.port.primary.UserServicePort;
import thi.cnd.userservice.primary.security.authentication.AuthenticatedAccount;

import java.util.function.Supplier;

@Component
public class UserInvitedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Setter
    private String companyIdParameterName = "";
    private static UserServicePort userServicePort;

    @Autowired
    public void setCompanyServicePort(UserServicePort userServicePort) {
        UserInvitedAuthorizationManager.userServicePort = userServicePort;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = (Authentication) authentication.get();
        String companyId = ((RequestAuthorizationContext) object).getVariables().get(this.companyIdParameterName);

        boolean decision = false;
        try {
            if((auth instanceof AuthenticatedAccount authenticatedAccount)) {
                User user = UserInvitedAuthorizationManager.userServicePort.findUserById(UserId.of(authenticatedAccount.getAccountId().toString()));
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

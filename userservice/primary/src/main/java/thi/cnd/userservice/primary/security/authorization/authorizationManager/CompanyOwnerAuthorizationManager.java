package thi.cnd.userservice.primary.security.authorization.authorizationManager;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.core.exception.CompanyNotFoundByIdException;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.port.primary.CompanyServicePort;
import thi.cnd.userservice.primary.security.authentication.AuthenticatedAccount;

import java.util.function.Supplier;

@Component
public class CompanyOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Setter
    private String companyIdParameterName;
    private static CompanyServicePort companyServicePort;

    @Autowired
    public void setCompanyServicePort(CompanyServicePort companyServicePort) {
        CompanyOwnerAuthorizationManager.companyServicePort = companyServicePort;
    }

    public CompanyOwnerAuthorizationManager() {}

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = (Authentication) authentication.get();
        String companyId = ((RequestAuthorizationContext) object).getVariables().get(this.companyIdParameterName);

        boolean decision = false;
        try {
            Company company = CompanyOwnerAuthorizationManager.companyServicePort.findCompanyById(CompanyId.of(companyId));
            if( (auth instanceof AuthenticatedAccount authenticatedAccount) &&
                (company.getOwner().toString().equals(authenticatedAccount.getAccountId().toString()))) {
                decision = true;
            }
        } catch (CompanyNotFoundByIdException ignored) {
            decision = false;
        }
        return new AuthorizationDecision(decision);
    }

    public static CompanyOwnerAuthorizationManager isOwner(String companyIdParameterName) {
        CompanyOwnerAuthorizationManager companyOwnerAuthorizationManager = new CompanyOwnerAuthorizationManager();
        companyOwnerAuthorizationManager.setCompanyIdParameterName(companyIdParameterName);
        return companyOwnerAuthorizationManager;
    }


}

package thi.cnd.userservice.adapters.in.security.authorization;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.domain.exceptions.CompanyNotFoundByIdException;
import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.UserId;
import thi.cnd.userservice.domain.CompanyService;
import thi.cnd.userservice.adapters.in.security.authentication.AuthenticatedAccount;

import java.util.function.Supplier;

@Component
public class CompanyMemberAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Setter
    private String companyIdParameterName = "";
    private static CompanyService companyService;

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        CompanyMemberAuthorizationManager.companyService = companyService;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = (Authentication) authentication.get();
        String companyId = ((RequestAuthorizationContext) object).getVariables().get(this.companyIdParameterName);

        boolean decision = false;
        try {
            Company company = CompanyMemberAuthorizationManager.companyService.findCompanyById(CompanyId.of(companyId));
            if( (auth instanceof AuthenticatedAccount authenticatedAccount) &&
                (company.getMembers().contains(UserId.of(authenticatedAccount.getAccountId().toString())))) {
                decision = true;
            }
        } catch (CompanyNotFoundByIdException ignored) {
            decision = false;
        }
        return new AuthorizationDecision(decision);
    }

    public static CompanyMemberAuthorizationManager isMember(String companyIdParameterName) {
        CompanyMemberAuthorizationManager companyMemberAuthorizationManager = new CompanyMemberAuthorizationManager();
        companyMemberAuthorizationManager.setCompanyIdParameterName(companyIdParameterName);
        return companyMemberAuthorizationManager;
    }


}

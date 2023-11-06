package thi.cnd.careerservice.http.security.authorization;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import thi.cnd.careerservice.company.CompanyPort;
import thi.cnd.careerservice.company.model.Company;
import thi.cnd.careerservice.company.model.CompanyId;
import thi.cnd.careerservice.exception.ResourceNotFoundException;
import thi.cnd.careerservice.http.security.authentication.AuthenticatedAccount;
import thi.cnd.careerservice.user.model.UserId;

@Component
public class CompanyMemberAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Setter
    private String companyIdParameterName = "";

    private static CompanyPort companyPort;

    @Autowired
    public void setCompanyServicePort(CompanyPort companyPort) {
        CompanyMemberAuthorizationManager.companyPort = companyPort;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        String companyId = object.getVariables().get(this.companyIdParameterName);
        boolean decision = false;
        try {
            Company company = companyPort.getCompany(new CompanyId(companyId));
            if( (auth instanceof AuthenticatedAccount authenticatedAccount) &&
                (company.members().contains(new UserId(authenticatedAccount.getAccountId())))) {
                decision = true;
            }
        } catch (ResourceNotFoundException ignored) {
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

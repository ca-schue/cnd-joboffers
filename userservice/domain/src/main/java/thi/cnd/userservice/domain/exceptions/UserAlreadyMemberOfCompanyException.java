package thi.cnd.userservice.domain.exceptions;

import thi.cnd.userservice.domain.model.company.CompanyId;

public class UserAlreadyMemberOfCompanyException extends Exception {
    public UserAlreadyMemberOfCompanyException(CompanyId companyId) {
        super("User already member of company " + companyId.toString());
    }
}

package thi.cnd.userservice.core.exception;

import thi.cnd.userservice.core.model.company.CompanyId;

public class UserAlreadyMemberOfCompanyException extends Exception {
    public UserAlreadyMemberOfCompanyException(CompanyId companyId) {
        super("User already member of company " + companyId.toString());
    }
}

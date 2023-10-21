package thi.cnd.userservice.core.exception;

import thi.cnd.userservice.core.model.company.CompanyId;

public class UserNotInvitedException extends Exception{
    public UserNotInvitedException(CompanyId companyId) {
        super("User was not invited to join company with id " + companyId.toString());
    }
}

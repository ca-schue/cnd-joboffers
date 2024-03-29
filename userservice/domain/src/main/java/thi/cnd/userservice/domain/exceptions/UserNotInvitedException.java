package thi.cnd.userservice.domain.exceptions;

import thi.cnd.userservice.domain.model.company.CompanyId;

public class UserNotInvitedException extends Exception{
    public UserNotInvitedException(CompanyId companyId) {
        super("User was not invited to join company with id " + companyId.toString());
    }
}

package thi.cnd.userservice.application.ports.out.event;

import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserId;

public interface UserEvents {
    void publishUserRegistered(User user);
    void publishUserDeleted(UserId userId);
    void publishUserInvitedToCompany(User user, Company company);
}

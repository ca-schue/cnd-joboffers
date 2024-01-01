package thi.cnd.userservice.adapters.out.kafka.user;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.adapters.generated.kafka.model.UserDeletedEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.UserInvitedToCompanyEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.UserRegisteredEvent;
import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserId;

@Component
public class UserEventMapper {

    UserRegisteredEvent toUserRegisteredEvent(User user) {
        return new UserRegisteredEvent()
                .userId(user.getId().getId())
                .firstName(user.getProfile().getFirstName())
                .email(user.getEmail());
    }

    UserDeletedEvent toUserDeletedEvent(UserId deletedUserId) {
        return new UserDeletedEvent()
                .userId(deletedUserId.getId());
    }

    UserInvitedToCompanyEvent toUserInvitedToCompanyEvent(User user, Company company){
        return new UserInvitedToCompanyEvent()
                .userId(user.getId().getId())
                .userEmail(user.getEmail())
                .userFirstName(user.getProfile().getFirstName())
                .companyId(company.getId().getId())
                .companyName(company.getDetails().name());
    }
}

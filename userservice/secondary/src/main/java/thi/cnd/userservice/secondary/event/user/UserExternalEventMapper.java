package thi.cnd.userservice.secondary.event.user;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.api.generated.model.ExternalUserEventDTO;
import thi.cnd.userservice.api.generated.model.UserDeletedEventDTO;
import thi.cnd.userservice.api.generated.model.UserInvitedToCompanyEventDTO;
import thi.cnd.userservice.api.generated.model.UserRegisteredEventDTO;
import thi.cnd.userservice.core.port.secondary.event.user.UserDeletedEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserInvitedToCompanyEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserRegisteredEvent;

@Component
public class UserExternalEventMapper {


    ExternalUserEventDTO toExternalEvent(UserEvent userEvent) {
        return switch (userEvent) {
            case UserRegisteredEvent event -> toExternalEvent(event);
            case UserDeletedEvent event -> toExternalEvent(event);
            case UserInvitedToCompanyEvent event -> toExternalEvent(event);
        };
    }

    private UserRegisteredEventDTO toExternalEvent(UserRegisteredEvent event) {
        return new UserRegisteredEventDTO()
                .userId(event.user().getId().getId())
                .firstName(event.user().getProfile().getFirstName())
                .email(event.user().getEmail());
    }

    private UserDeletedEventDTO toExternalEvent(UserDeletedEvent event) {
        return new UserDeletedEventDTO()
                .userId(event.userId().getId());
    }

    private UserInvitedToCompanyEventDTO toExternalEvent(UserInvitedToCompanyEvent event) {
        return new UserInvitedToCompanyEventDTO()
                .userId(event.user().getId().getId())
                .userEmail(event.user().getEmail())
                .userFirstName(event.user().getProfile().getFirstName())
                .companyId(event.company().getId().getId())
                .companyName(event.company().getDetails().name());
    }

}

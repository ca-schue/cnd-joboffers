package thi.cnd.userservice.core.port.secondary.event.user;

import thi.cnd.userservice.core.port.secondary.event.EventTopics;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.user.User;

public record UserInvitedToCompanyEvent(User user, Company company) implements UserEvent {
    @Override
    public EventTopics getTopic() {
        return EventTopics.USER_INVITED_TO_COMPANY;
    }
}
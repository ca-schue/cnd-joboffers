package thi.cnd.userservice.core.port.secondary.event.user;

import thi.cnd.userservice.core.port.secondary.event.EventTopics;
import thi.cnd.userservice.core.model.user.User;

public record UserRegisteredEvent(User user) implements UserEvent {
    @Override
    public EventTopics getTopic() {
        return EventTopics.USER_REGISTERED;
    }
}

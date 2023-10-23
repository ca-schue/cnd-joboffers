package thi.cnd.userservice.core.port.secondary.event.user;

import thi.cnd.userservice.core.port.secondary.event.EventTopics;
import thi.cnd.userservice.core.model.user.UserId;

public record UserDeletedEvent(UserId userId) implements UserEvent {
    @Override
    public EventTopics getTopic() {
        return EventTopics.USER_DELETED;
    }
}
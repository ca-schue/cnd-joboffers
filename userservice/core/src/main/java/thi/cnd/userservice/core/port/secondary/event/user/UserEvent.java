package thi.cnd.userservice.core.port.secondary.event.user;

import thi.cnd.userservice.core.port.secondary.event.Event;

public sealed interface UserEvent extends Event permits UserDeletedEvent, UserInvitedToCompanyEvent, UserRegisteredEvent {

}

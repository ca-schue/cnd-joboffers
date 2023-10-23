package thi.cnd.userservice.core.port.secondary.event.user;

public interface UserEventPort {
    void sendEvent(UserEvent event);
}

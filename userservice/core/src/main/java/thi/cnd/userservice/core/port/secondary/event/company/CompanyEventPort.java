package thi.cnd.userservice.core.port.secondary.event.company;

public interface CompanyEventPort {
    void sendEvent(CompanyEvent event);
}

package thi.cnd.userservice.core.port.secondary.event.company;

import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.port.secondary.event.EventTopics;

public record CompanyRegisteredEvent(CompanyId companyId, User owner, String companyName) implements CompanyEvent {
    @Override
    public EventTopics getTopic() {
        return EventTopics.COMPANY_REGISTERED;
    }
}


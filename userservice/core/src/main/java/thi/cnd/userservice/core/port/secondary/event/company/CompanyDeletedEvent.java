package thi.cnd.userservice.core.port.secondary.event.company;

import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.port.secondary.event.EventTopics;

public record CompanyDeletedEvent(CompanyId companyId) implements CompanyEvent {
    @Override
    public EventTopics getTopic() {
        return EventTopics.COMPANY_DELETED;
    }
}
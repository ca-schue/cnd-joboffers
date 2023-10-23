package thi.cnd.userservice.secondary.event.company;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.api.generated.model.*;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyDeletedEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyNameChangedEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyRegisteredEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserDeletedEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserInvitedToCompanyEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserRegisteredEvent;

@Component
public class CompanyExternalEventMapper {


    ExternalUserEventDTO toExternalEvent(CompanyEvent userEvent) {
        return switch (userEvent) {
            case CompanyDeletedEvent event -> toExternalEvent(event);
            case CompanyNameChangedEvent event -> toExternalEvent(event);
            case CompanyRegisteredEvent event -> toExternalEvent(event);
        };
    }

    private CompanyRegisteredEventDTO toExternalEvent(CompanyRegisteredEvent event) {
        return new CompanyRegisteredEventDTO()
                .companyId(event.companyId().getId())
                .ownerId(event.owner().getId().getId())
                .ownerEmail(event.owner().getEmail())
                .companyName(event.companyName());
    }

    private CompanyNameChangedEventDTO toExternalEvent(CompanyNameChangedEvent event) {
        return new CompanyNameChangedEventDTO()
                .companyId(event.companyId().getId())
                .name(event.newName());
    }

    private CompanyDeletedEventDTO toExternalEvent(CompanyDeletedEvent event) {
        return new CompanyDeletedEventDTO()
                .companyId(event.companyId().getId());
    }

}

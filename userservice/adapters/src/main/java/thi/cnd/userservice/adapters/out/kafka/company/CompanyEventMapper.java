package thi.cnd.userservice.adapters.out.kafka.company;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.adapters.generated.kafka.model.CompanyDeletedEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.CompanyNameChangedEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.CompanyRegisteredEvent;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.User;

@Component
public class CompanyEventMapper {

    CompanyDeletedEvent toCompanyDeletedEvent(CompanyId deletedCompanyId) {
        return new CompanyDeletedEvent()
                .companyId(deletedCompanyId.getId());
    }

    CompanyNameChangedEvent toCompanyNameChangedEvent(CompanyId companyId, String updatedCompanyName) {
        return new CompanyNameChangedEvent()
                .companyId(companyId.getId())
                .name(updatedCompanyName);
    }

    CompanyRegisteredEvent toCompanyRegisteredEvent(CompanyId registeredCompanyId, User owner, String companyName) {
        return new CompanyRegisteredEvent()
                .companyId(registeredCompanyId.getId())
                .ownerId(owner.getId().getId())
                .ownerEmail(owner.getEmail())
                .companyName(companyName);
    }
}

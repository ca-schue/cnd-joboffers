package thi.cnd.userservice.adapters.out.event.company;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.adapters.generated.kafka.model.*;
import thi.cnd.userservice.adapters.out.event.config.EventTopics;
import thi.cnd.userservice.application.ports.out.event.CompanyEvents;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.User;

@Component
@RequiredArgsConstructor
public class CompanyKafkaEvents implements CompanyEvents {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CompanyEventMapper companyEventMapper;

    @Override
    public void publishCompanyDeleted(CompanyId companyId) {
        CompanyDeletedEvent companyDeletedEvent = companyEventMapper.toCompanyDeletedEvent(companyId);
        kafkaTemplate.send(EventTopics.COMPANY_DELETED.getName(), companyDeletedEvent);
    }

    @Override
    public void publishCompanyRegistered(CompanyId registeredCompanyId, User owner, String companyName) {
        CompanyRegisteredEvent companyRegisteredEvent = companyEventMapper.toCompanyRegisteredEvent(registeredCompanyId, owner, companyName);
        kafkaTemplate.send(EventTopics.COMPANY_REGISTERED.getName(), companyRegisteredEvent);
    }

    @Override
    public void publishCompanyNameChanged(CompanyId companyId, String updatedCompanyName) {
        CompanyNameChangedEvent companyNameChangedEvent = companyEventMapper.toCompanyNameChangedEvent(companyId, updatedCompanyName);
        kafkaTemplate.send(EventTopics.COMPANY_NAME_CHANGED.getName(), companyNameChangedEvent);
    }
}

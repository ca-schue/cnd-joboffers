package thi.cnd.userservice.secondary.event.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.api.generated.model.ExternalUserEventDTO;
import thi.cnd.userservice.core.port.secondary.event.EventTopics;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyEvent;
import thi.cnd.userservice.core.port.secondary.event.company.CompanyEventPort;
import thi.cnd.userservice.secondary.event.KafkaJsonPublisher;

@Component
@RequiredArgsConstructor
public class CompanyEventAdapter implements CompanyEventPort {

    private final KafkaJsonPublisher kafkaJsonPublisher;
    private final CompanyExternalEventMapper eventMapper;

    @Override
    public void sendEvent(CompanyEvent companyEvent) {
        EventTopics topic = companyEvent.getTopic();
        ExternalUserEventDTO eventDTO = eventMapper.toExternalEvent(companyEvent);
        kafkaJsonPublisher.sendEvent(
                topic,
                eventDTO
        );
    }
}

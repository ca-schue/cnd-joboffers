package thi.cnd.userservice.secondary.event;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.api.generated.model.ExternalUserEventDTO;
import thi.cnd.userservice.core.port.secondary.event.EventTopics;

@Component
@AllArgsConstructor
public class KafkaJsonPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(EventTopics topic, ExternalUserEventDTO data) {
        kafkaTemplate.send(topic.getName(), data);
    }
}

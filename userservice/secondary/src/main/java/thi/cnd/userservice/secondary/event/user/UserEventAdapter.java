package thi.cnd.userservice.secondary.event.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.api.generated.model.ExternalUserEventDTO;
import thi.cnd.userservice.core.port.secondary.event.EventTopics;
import thi.cnd.userservice.core.port.secondary.event.user.UserEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserEventPort;
import thi.cnd.userservice.secondary.event.KafkaJsonPublisher;

@Component
@RequiredArgsConstructor
public class UserEventAdapter implements UserEventPort {
    private final KafkaJsonPublisher kafkaJsonPublisher;
    private final UserExternalEventMapper eventMapper;

    @Override
    public void sendEvent(UserEvent userEvent) {
        EventTopics topic = userEvent.getTopic();
        ExternalUserEventDTO eventDTO = eventMapper.toExternalEvent(userEvent);
        kafkaJsonPublisher.sendEvent(
                topic,
                eventDTO
        );
    }

}

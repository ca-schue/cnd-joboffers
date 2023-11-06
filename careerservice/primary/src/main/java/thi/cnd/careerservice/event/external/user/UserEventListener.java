package thi.cnd.careerservice.event.external.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import thi.cnd.careerservice.shared.event.ExternalEventListener;
import thi.cnd.careerservice.jobapplication.domain.JobApplicationCrossAggregateEventHandler;
import thi.cnd.careerservice.user.model.UserId;
import thi.cnd.careerservice.event.userservice.generated.model.UserDeletedEventDTO;
import lombok.RequiredArgsConstructor;

/**
 * Listens to external events from the user-service, which concerns a user
 */
@Component
@RequiredArgsConstructor
public class UserEventListener implements ExternalEventListener {

    private final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    private final JobApplicationCrossAggregateEventHandler eventHandler;

    @KafkaListener(groupId = "career-service", topics = "user-service_user-deleted")
    public void listen(@Payload UserDeletedEventDTO event) {
        logger.info("User-Deleted-Event received for user {}", event.getUserId());
        eventHandler.deleteJobApplication(new UserId(event.getUserId()));
    }

}

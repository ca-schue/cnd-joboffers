package thi.cnd.authservice.primary.event.user;



import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.core.model.account.AccountId;
import thi.cnd.authservice.core.ports.primary.AccountServicePort;
import thi.cnd.authservice.events.generated.model.UserDeletedEventDTO;
import thi.cnd.authservice.events.generated.model.UserRegisteredEventDTO;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final Logger logger = LoggerFactory.getLogger(UserEventListener.class);

    private final AccountServicePort accountServicePort;

    @KafkaListener(groupId = "auth-service", topics = "user-service_user-registered")
    public void listen(@Payload UserRegisteredEventDTO event) {
        logger.info("User-Registered-Event received for user {}", event.getUserId());
        accountServicePort.validateAccount(AccountId.of(event.getUserId()));
    }

    @KafkaListener(groupId = "auth-service", topics = "user-service_user-deleted")
    public void listen(@Payload UserDeletedEventDTO event) {
        logger.info("User-Deleted-Event received for user {}", event.getUserId());
        accountServicePort.invalidateAccount(AccountId.of(event.getUserId()));
    }
}

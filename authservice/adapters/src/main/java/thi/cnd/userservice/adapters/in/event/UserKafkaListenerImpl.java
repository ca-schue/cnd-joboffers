package thi.cnd.userservice.adapters.in.event;



import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import thi.cnd.authservice.domain.AccountService;
import thi.cnd.authservice.domain.model.account.AccountId;
import thi.cnd.authservice.events.generated.model.UserDeletedEventDTO;
import thi.cnd.authservice.events.generated.model.UserRegisteredEventDTO;

@Component
@RequiredArgsConstructor
public class UserKafkaListenerImpl {

    private final Logger logger = LoggerFactory.getLogger(UserKafkaListenerImpl.class);

    private final AccountService accountService;

    @KafkaListener(groupId = "auth-service", topics = "user-service_user-registered")
    public void listen(@Payload UserRegisteredEventDTO event) {
        logger.info("User-Registered-Event received for user {}", event.getUserId());
        accountService.validateAccount(AccountId.of(event.getUserId()));
    }

    @KafkaListener(groupId = "auth-service", topics = "user-service_user-deleted")
    public void listen(@Payload UserDeletedEventDTO event) {
        logger.info("User-Deleted-Event received for user {}", event.getUserId());
        accountService.invalidateAccount(AccountId.of(event.getUserId()));
    }
}

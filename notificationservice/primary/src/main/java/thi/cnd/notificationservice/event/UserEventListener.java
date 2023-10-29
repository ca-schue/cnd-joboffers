package thi.cnd.notificationservice.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import thi.cnd.notificationservice.api.generated.model.CompanyRegisteredEventDTO;
import thi.cnd.notificationservice.api.generated.model.UserInvitedToCompanyEventDTO;
import thi.cnd.notificationservice.api.generated.model.UserRegisteredEventDTO;
import thi.cnd.notificationservice.user.UserNotificationService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final UserNotificationService userNotificationService;

    @KafkaListener(groupId = "notification-service", topics = "user-service_user-registered")
    public void listen(@Payload UserRegisteredEventDTO event) {
        userNotificationService.sendWelcomeEmail(event.getEmail(), event.getFirstName());
    }

    @KafkaListener(groupId = "notification-service", topics = "user-service_company-registered")
    public void listen(@Payload CompanyRegisteredEventDTO event) {
        userNotificationService.sendEmailOnCompanyRegistration(event.getOwnerEmail(), event.getCompanyName());
    }

    @KafkaListener(groupId = "notification-service", topics = "user-service_user-invited-to-company")
    public void listen(@Payload UserInvitedToCompanyEventDTO event) {
        userNotificationService.sendEmailOnUserInvitationToCompany(event.getUserEmail(), event.getUserFirstName(), event.getCompanyName());
    }

}

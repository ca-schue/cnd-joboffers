package thi.cnd.userservice.adapters.out.kafka.user;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import thi.cnd.userservice.adapters.generated.kafka.model.UserDeletedEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.UserInvitedToCompanyEvent;
import thi.cnd.userservice.adapters.generated.kafka.model.UserRegisteredEvent;
import thi.cnd.userservice.adapters.out.kafka.config.EventTopics;
import thi.cnd.userservice.application.ports.out.event.UserEvents;
import thi.cnd.userservice.domain.model.company.Company;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserId;

@Component
@RequiredArgsConstructor
public class UserKafkaEvents implements UserEvents {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserEventMapper userEventMapper;

    @Override
    public void publishUserRegistered(User registeredUser) {
        UserRegisteredEvent userRegisteredEvent = userEventMapper.toUserRegisteredEvent(registeredUser);
        kafkaTemplate.send(EventTopics.USER_REGISTERED.getName(), userRegisteredEvent);
    }

    @Override
    public void publishUserDeleted(UserId deletedUserId) {
        UserDeletedEvent userDeletedEvent = userEventMapper.toUserDeletedEvent(deletedUserId);
        kafkaTemplate.send(EventTopics.USER_DELETED.getName(), userDeletedEvent);
    }

    @Override
    public void publishUserInvitedToCompany(User user, Company company) {
        UserInvitedToCompanyEvent userInvitedCompanyEvent = userEventMapper.toUserInvitedToCompanyEvent(user, company);
        kafkaTemplate.send(EventTopics.USER_INVITED_TO_COMPANY.getName(), userInvitedCompanyEvent);
    }

}

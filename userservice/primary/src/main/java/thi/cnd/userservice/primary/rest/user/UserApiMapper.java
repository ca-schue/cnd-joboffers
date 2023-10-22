package thi.cnd.userservice.primary.rest.user;

import org.mapstruct.*;
import thi.cnd.userservice.api.generated.model.*;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;

import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserApiMapper {

    default UserDTO toDTO(User user) {
        return new UserDTO()
                .id(user.getId().getId())
                .profile(
                        new UserProfileDTO()
                                .userProfileEmail(user.getEmail())
                                .firstName(user.getProfile().getFirstName())
                                .lastName(user.getProfile().getLastName())
                        )
                .associations(
                        new UserCompanyAssociationDTO()
                                .memberOf(user.getAssociations().memberOf().stream().map(CompanyId::getId).collect(Collectors.toList()))
                                .invitedTo(user.getAssociations().invitedTo().stream().map(CompanyId::getId).collect(Collectors.toList()))
                        )
                .settings(
                        new UserSettingsDTO()
                                .nightModeActive(user.getSettings().nightModeActive())
                        )
                .subscription(
                        new UserSubscriptionDTO()
                                .subscribed(user.getSubscription().isSubscribed())
                                .subscribedUntil(user.getSubscription().subscribedUntil())
                        );
    }

    default PublicUserProfileDTO toPublicProfileDTO(User user) {
        return new PublicUserProfileDTO()
                .id(user.getId().getId())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName());
    }

}

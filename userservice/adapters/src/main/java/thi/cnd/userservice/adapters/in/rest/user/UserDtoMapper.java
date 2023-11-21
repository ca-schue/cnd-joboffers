package thi.cnd.userservice.adapters.in.rest.user;

import org.mapstruct.*;
import thi.cnd.authservice.adapters.generated.rest.model.*;
import thi.cnd.userservice.adapters.generated.rest.model.*;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.model.user.User;

import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserDtoMapper {

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
                                .memberOf(user.getAssociations().getMemberOf().stream().map(CompanyId::getId).collect(Collectors.toList()))
                                .invitedTo(user.getAssociations().getInvitedTo().stream().map(CompanyId::getId).collect(Collectors.toList()))
                                .ownerOf(user.getAssociations().getOwnerOf() != null ? user.getAssociations().getOwnerOf().getId() : null)
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
        return new PublicUserProfileDTO(
                user.getId().getId(),
                user.getProfile().getFirstName(),
                user.getProfile().getLastName(),
                user.getEmail()
        );
    }

}

package thi.cnd.userservice.secondary.repository.user.model;

import org.springframework.stereotype.Component;
import thi.cnd.userservice.core.model.user.*;

@Component
public class UserDaoMapper {

    public User toUser(UserDAO dao) {
        return new User(
                dao.id(),
                new UserCompanyAssociation(
                        dao.associations().memberOf(),
                        dao.associations().invitedTo(),
                        dao.associations().ownerOf()
                ),
                new UserProfile(
                        dao.profile().email(),
                        dao.profile().firstName(),
                        dao.profile().lastName()
                ),
                new UserSettings(
                    dao.settings().nightModeActive()
                ),new UserSubscription(
                    dao.subscription().subscribedUntil()
                )
        );
    }

    public UserDAO toDAO(User user) {
        return new UserDAO(
                user.getId(),
                new UserProfileDAO(
                    user.getProfile().getEmail(),
                    user.getProfile().getFirstName(),
                    user.getProfile().getLastName()
                ),
                new UserCompanyAssociationDAO(
                    user.getAssociations().getMemberOf(),
                    user.getAssociations().getInvitedTo(),
                    user.getAssociations().getOwnerOf()
                ),
                new UserSettingsDAO(
                    user.getSettings().nightModeActive()
                ),
                new UserSubscriptionDAO(
                    user.getSubscription().subscribedUntil()
                )
        );
    }
}

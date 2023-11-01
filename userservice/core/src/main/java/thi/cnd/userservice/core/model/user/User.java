package thi.cnd.userservice.core.model.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.UserAlreadyMemberOfCompanyException;
import thi.cnd.userservice.core.exception.UserNotInvitedException;

import java.time.Duration;

@Getter
@Setter
@AllArgsConstructor
public class User {

    @NotNull UserId id;
    @NotNull UserCompanyAssociation associations;
    @NotNull UserProfile profile;
    @NotNull UserSettings settings;
    @NotNull UserSubscription subscription;

    public User(UserId userId, String email, String firstName, String lastName) {
        this(
            userId,
            new UserCompanyAssociation(),
            new UserProfile(email, firstName, lastName),
            new UserSettings(),
            new UserSubscription()
        );
    }

    public User extendUserSubscription(@NotNull Duration extendBy) {
        this.subscription = this.subscription.extendBy(extendBy);
        return this;
    }

    public void updateUserDetails(@NotNull UserProfile details) {
        this.profile = details;
    }

    public void updateUserSettings(@NotNull UserSettings settings) {
        this.settings = settings;
    }

    public void addInvitationToCompany(@NotNull CompanyId companyId) throws UserAlreadyMemberOfCompanyException {
        this.associations.inviteTo(companyId);
    }

    public void acceptCompanyInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        this.associations.acceptCompanyInvitation(companyId);
    }

    public void removeInvitation(@NotNull CompanyId companyId) throws UserNotInvitedException {
        this.associations.removeInvitation(companyId);
    }

    public void updateEmail(String email) {
        profile.setEmail(email);
    }

    public void addOwnershipOfCompany(CompanyId companyId) {
        this.associations.addOwnerOfCompany(companyId);
    }

    public String getEmail() {
        return getProfile().getEmail();
    }

}

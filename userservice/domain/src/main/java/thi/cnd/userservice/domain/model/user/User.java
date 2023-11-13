package thi.cnd.userservice.domain.model.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import thi.cnd.userservice.domain.exceptions.UserAlreadyMemberOfCompanyException;
import thi.cnd.userservice.domain.exceptions.UserNotInvitedException;
import thi.cnd.userservice.domain.model.company.CompanyId;

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

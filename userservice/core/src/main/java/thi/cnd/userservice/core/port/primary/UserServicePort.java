package thi.cnd.userservice.core.port.primary;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.model.user.UserProfile;
import thi.cnd.userservice.core.model.user.UserSettings;

import java.time.Duration;

public interface UserServicePort {

    @NotNull User findUserById(UserId userId) throws UserNotFoundByIdException;

    @NotNull User findUserByEmail(String email) throws UserNotFoundByEmailException;

    @NotNull User registerNewUser(UserId userId, String email, String firstName, String lastName) throws UserAlreadyExistsException;

    @NotNull User updateUserSettings(UserId userId, UserSettings updatedUserSettings) throws UserNotFoundByIdException;

    @NotNull User updateUserProfile(UserId userId, UserProfile updatedUserProfile) throws UserNotFoundByIdException, EmailAlreadyInUseException;

    @NotNull User extendUserSubscription(UserId userId, Duration extendBy) throws UserNotFoundByIdException;

    void deleteUser(UserId userId) throws UserNotFoundByIdException;

    void inviteUserToCompany(CompanyId companyId, String userEmail) throws UserNotFoundByEmailException, UserAlreadyMemberOfCompanyException, UserNotFoundByIdException, CompanyNotFoundByIdException;

    void acceptCompanyInvitation(UserId userId, CompanyId companyId) throws UserNotFoundByIdException, UserAlreadyMemberOfCompanyException, UserNotInvitedException, CompanyNotFoundByIdException;

    User updateEmail(UserId userId, String email) throws UserNotFoundByIdException;

    void deleteUser(@Email String email) throws UserNotFoundByEmailException, UserNotFoundByIdException;

}

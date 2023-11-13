package thi.cnd.userservice.domain;

import jakarta.validation.constraints.NotNull;
import thi.cnd.userservice.domain.model.company.CompanyId;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.user.User;
import thi.cnd.userservice.domain.model.user.UserId;
import thi.cnd.userservice.domain.model.user.UserProfile;
import thi.cnd.userservice.domain.model.user.UserSettings;

import java.time.Duration;

public interface UserService {

    @NotNull User findUserById(UserId userId) throws UserNotFoundByIdException;

    @NotNull User findUserByEmail(String email) throws UserNotFoundByEmailException;

    @NotNull User registerNewUser(UserId userId, String email, String firstName, String lastName) throws UserAlreadyExistsException;

    @NotNull User updateUserSettings(UserId userId, UserSettings updatedUserSettings) throws UserNotFoundByIdException;

    @NotNull User updateUserProfile(UserId userId, UserProfile updatedUserProfile) throws UserNotFoundByIdException, EmailAlreadyInUseException;

    @NotNull User extendUserSubscription(UserId userId, Duration extendBy) throws UserNotFoundByIdException;

    void deleteUser(UserId userId) throws UserNotFoundByIdException, CompanyNotFoundByIdException;

    void inviteUserToCompany(CompanyId companyId, String userEmail) throws UserNotFoundByEmailException, UserAlreadyMemberOfCompanyException, UserNotFoundByIdException, CompanyNotFoundByIdException;

    void acceptCompanyInvitation(UserId userId, CompanyId companyId) throws UserNotFoundByIdException, UserAlreadyMemberOfCompanyException, UserNotInvitedException, CompanyNotFoundByIdException;

    User updateEmail(UserId userId, String email) throws UserNotFoundByIdException;


}

package thi.cnd.userservice.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.ports.primary.CompanyServicePort;
import thi.cnd.userservice.core.ports.primary.UserServicePort;
import thi.cnd.userservice.core.ports.secondary.UserRepositoryPort;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.model.user.UserProfile;
import thi.cnd.userservice.core.model.user.UserSettings;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final CompanyServicePort companyServicePort;

    @Override
    public User findUserById(UserId userId) throws UserNotFoundByIdException {
        return userRepositoryPort.findUserById(userId);
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundByEmailException {
        return userRepositoryPort.findUserByEmail(email);
    }

    @Override
    public User registerNewUser(UserId userId, String email, String firstName, String lastName) throws UserAlreadyExistsException {
        // TODO: Input verification?
        User newUser = new User(userId, email, firstName, lastName);
        User savedUser = userRepositoryPort.saveUser(newUser);

        // TODO: eventService.sendEvent(new UserRegisteredEvent(savedUser));

        return savedUser;
    }

    @Override
    public User updateUserProfile(UserId userId, UserProfile userProfile) throws UserNotFoundByIdException {
        // TODO: Input verification?
        User user = userRepositoryPort.findUserById(userId);
        user.setProfile(userProfile);
        return userRepositoryPort.updateOrSaveUser(user);
    }

    @Override
    public User updateUserSettings(UserId userId, UserSettings userSettings) throws UserNotFoundByIdException {
        User user = userRepositoryPort.findUserById(userId);
        user.setSettings(userSettings); // TODO: Input verification?
        return userRepositoryPort.updateOrSaveUser(user);
    }

    @Override
    public User updateEmail(UserId userId, String email) throws UserNotFoundByIdException {
        User user = userRepositoryPort.findUserById(userId);
        user.getProfile().setEmail(email);
        return userRepositoryPort.updateOrSaveUser(user);
    }

    @Override
    public User extendUserSubscription(UserId userId, Duration extendBy) throws UserNotFoundByIdException {
        User user = userRepositoryPort.findUserById(userId);
        user.extendUserSubscription(extendBy);
        return userRepositoryPort.updateOrSaveUser(user);
    }

    @Override
    public void deleteUser(UserId userId) throws UserNotFoundByIdException {
        userRepositoryPort.deleteUserById(userId);
        // TODO: eventService.sendEvent(new UserDeletedEvent(userId));
    }

    @Override
    public void inviteUserToCompany(CompanyId companyId, String userEmail) throws UserNotFoundByEmailException, UserAlreadyMemberOfCompanyException {
        Company company = companyServicePort.findCompanyById(companyId);
        User user = userRepositoryPort.findUserByEmail(userEmail);
        user.addInvitationToCompany(companyId);
        userRepositoryPort.updateOrSaveUser(user);
        // TODO: eventService.sendEvent(new UserInvitedToCompanyEvent(user, company));
    }

    @Override
    public void acceptCompanyInvitation(UserId userId, CompanyId companyId) throws UserNotFoundByIdException, UserAlreadyMemberOfCompanyException, UserNotInvitedException {
        User user = userRepositoryPort.findUserById(userId);
        user.acceptCompanyInvitation(companyId);
        companyServicePort.addMember(companyId, userId);
        userRepositoryPort.updateOrSaveUser(user);
    }

    @Override
    public void deleteUser(String email) throws UserNotFoundByEmailException {
        userRepositoryPort.deleteUserByEmail(email);
        // TODO: eventService.sendEvent(new UserDeletedEvent(userId));
    }
}

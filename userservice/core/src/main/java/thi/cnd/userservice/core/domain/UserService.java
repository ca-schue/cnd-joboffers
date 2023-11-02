package thi.cnd.userservice.core.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.core.model.company.Company;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.port.primary.CompanyServicePort;
import thi.cnd.userservice.core.port.primary.UserServicePort;
import thi.cnd.userservice.core.port.secondary.event.user.UserEventPort;
import thi.cnd.userservice.core.port.secondary.repository.UserRepositoryPort;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.model.user.UserProfile;
import thi.cnd.userservice.core.model.user.UserSettings;
import thi.cnd.userservice.core.port.secondary.event.user.UserDeletedEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserInvitedToCompanyEvent;
import thi.cnd.userservice.core.port.secondary.event.user.UserRegisteredEvent;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final CompanyServicePort companyServicePort;
    private final UserEventPort userEventPort;

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

        userEventPort.sendEvent(new UserRegisteredEvent(savedUser));

        return savedUser;
    }

    @Override
    public User updateUserProfile(UserId userId, UserProfile updatedUserProfile) throws UserNotFoundByIdException, EmailAlreadyInUseException {
        // TODO: Input verification?
        User user = userRepositoryPort.findUserById(userId);

        // Input verification:
        String newUserProfileEmail = updatedUserProfile.getEmail();
        try {
            User userWithSameEmail = userRepositoryPort.findUserByEmail(newUserProfileEmail);
            if (!userWithSameEmail.getId().toString().equals(userId.toString())) {
                throw new EmailAlreadyInUseException(newUserProfileEmail);
            } else {
                throw new UserNotFoundByEmailException(""); // TODO: ugly solution
            }
        } catch (UserNotFoundByEmailException e) {
            user.setProfile(updatedUserProfile);
            return userRepositoryPort.updateOrSaveUser(user);
        }
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
    public void deleteUser(UserId userId) throws UserNotFoundByIdException, CompanyNotFoundByIdException {
        User userTbd = userRepositoryPort.findUserById(userId);
        if(userTbd.getAssociations().getOwnerOf() != null) {
            companyServicePort.deleteCompanyById(userTbd.getAssociations().getOwnerOf());
        }
        companyServicePort.deleteUserFromAllCompanies(userId);
        userRepositoryPort.deleteUserById(userId);
        userEventPort.sendEvent(new UserDeletedEvent(userId));
    }

    @Override
    public void inviteUserToCompany(CompanyId companyId, String userEmail) throws UserNotFoundByEmailException, CompanyNotFoundByIdException, UserAlreadyMemberOfCompanyException {
        Company company = companyServicePort.findCompanyById(companyId); // Verification if company exists TODO: Redundant?
        User user = userRepositoryPort.findUserByEmail(userEmail);
        user.addInvitationToCompany(companyId);
        userRepositoryPort.updateOrSaveUser(user);
        userEventPort.sendEvent(new UserInvitedToCompanyEvent(user, company));
    }

    @Override
    public void acceptCompanyInvitation(UserId userId, CompanyId companyId) throws UserNotFoundByIdException, CompanyNotFoundByIdException, UserNotInvitedException  {
        User user = userRepositoryPort.findUserById(userId);
        user.acceptCompanyInvitation(companyId);
        companyServicePort.addMember(companyId, userId);
        userRepositoryPort.updateOrSaveUser(user);
    }

}

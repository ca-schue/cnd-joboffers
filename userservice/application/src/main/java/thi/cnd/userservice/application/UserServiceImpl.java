package thi.cnd.userservice.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.*;
import thi.cnd.userservice.application.ports.out.event.UserEvents;
import thi.cnd.userservice.application.ports.out.repository.UserRepository;
import java.time.Duration;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final UserEvents userEvents;

    @Override
    public User findUserById(UserId userId) throws UserNotFoundByIdException {
        return userRepository.findUserById(userId);
    }

    @Override
    public User findUserByEmail(String email) throws UserNotFoundByEmailException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User registerNewUser(UserId userId, String email, String firstName, String lastName) throws UserAlreadyExistsException {
        // TODO: Input verification?
        User newUser = new User(userId, email, firstName, lastName);
        User savedUser = userRepository.saveUser(newUser);

        userEvents.publishUserRegistered(savedUser);

        return savedUser;
    }

    @Override
    public User updateUserProfile(UserId userId, UserProfile updatedUserProfile) throws UserNotFoundByIdException, EmailAlreadyInUseException {
        // TODO: Input verification?
        User user = userRepository.findUserById(userId);

        // Input verification:
        String newUserProfileEmail = updatedUserProfile.getEmail();
        try {
            User userWithSameEmail = userRepository.findUserByEmail(newUserProfileEmail);
            if (!userWithSameEmail.getId().toString().equals(userId.toString())) {
                throw new EmailAlreadyInUseException(newUserProfileEmail);
            } else {
                throw new UserNotFoundByEmailException(""); // TODO: ugly solution ...
            }
        } catch (UserNotFoundByEmailException e) {
            user.setProfile(updatedUserProfile);
            return userRepository.updateOrSaveUser(user);
        }
    }

    @Override
    public User updateUserSettings(UserId userId, UserSettings userSettings) throws UserNotFoundByIdException {
        User user = userRepository.findUserById(userId);
        user.setSettings(userSettings); // TODO: Input verification?
        return userRepository.updateOrSaveUser(user);
    }

    @Override
    public User updateEmail(UserId userId, String email) throws UserNotFoundByIdException {
        User user = userRepository.findUserById(userId);
        user.getProfile().setEmail(email);
        return userRepository.updateOrSaveUser(user);
    }

    @Override
    public User extendUserSubscription(UserId userId, Duration extendBy) throws UserNotFoundByIdException {
        User user = userRepository.findUserById(userId);
        user.extendUserSubscription(extendBy);
        return userRepository.updateOrSaveUser(user);
    }

    @Override
    public void deleteUser(UserId userId) throws UserNotFoundByIdException, CompanyNotFoundByIdException {
        User userTbd = userRepository.findUserById(userId);
        if(userTbd.getAssociations().getOwnerOf() != null) {
            companyService.deleteCompanyById(userTbd.getAssociations().getOwnerOf());
        }
        companyService.deleteUserFromAllCompanies(userId);
        userRepository.deleteUserById(userId);
        userEvents.publishUserDeleted(userId);
    }

    @Override
    public void inviteUserToCompany(CompanyId companyId, String userEmail) throws UserNotFoundByEmailException, CompanyNotFoundByIdException, UserAlreadyMemberOfCompanyException {
        Company company = companyService.findCompanyById(companyId); // Verification if company exists TODO: Redundant?
        User user = userRepository.findUserByEmail(userEmail);
        user.addInvitationToCompany(companyId);
        userRepository.updateOrSaveUser(user);
        userEvents.publishUserInvitedToCompany(user, company);
    }

    @Override
    public void acceptCompanyInvitation(UserId userId, CompanyId companyId) throws UserNotFoundByIdException, CompanyNotFoundByIdException, UserNotInvitedException {
        User user = userRepository.findUserById(userId);
        user.acceptCompanyInvitation(companyId);
        companyService.addMember(companyId, userId);
        userRepository.updateOrSaveUser(user);
    }

}

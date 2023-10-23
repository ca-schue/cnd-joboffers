package thi.cnd.userservice.primary.rest.user;

import thi.cnd.userservice.api.generated.UserApi;
import thi.cnd.userservice.api.generated.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.userservice.core.exception.*;
import thi.cnd.userservice.core.model.company.CompanyId;
import thi.cnd.userservice.core.model.user.User;
import thi.cnd.userservice.core.model.user.UserId;
import thi.cnd.userservice.core.model.user.UserProfile;
import thi.cnd.userservice.core.model.user.UserSettings;
import thi.cnd.userservice.core.port.primary.UserServicePort;
import thi.cnd.userservice.primary.security.authentication.AuthenticatedAccount;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserServicePort userServicePort;
    private final UserApiMapper userApiMapper;

    @Override
    public ResponseEntity<Void> acceptInvitation(UUID userId, AcceptCompanyInvitationRequestDTO requestDTO) {
        try {
            userServicePort.acceptCompanyInvitation(new UserId(userId), new CompanyId(requestDTO.getCompanyId()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundByIdException | CompanyNotFoundByIdException e1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (UserAlreadyMemberOfCompanyException | UserNotInvitedException e2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e2.getMessage());
        }

    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        try {
            userServicePort.deleteUser(new UserId(userId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PublicUserProfileDTO> getPublicUserProfile(UUID userId) {
        try {
            User user = userServicePort.findUserById(new UserId(userId));
            return ResponseEntity.ok(userApiMapper.toPublicProfileDTO(user));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> getUser(UUID userId) {
        try {
            User user = userServicePort.findUserById(new UserId(userId));
            return ResponseEntity.ok(userApiMapper.toDTO(user));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> registerNewUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        AuthenticatedAccount authAcc = (AuthenticatedAccount) SecurityContextHolder.getContext().getAuthentication();
        try {
            User registeredUser = userServicePort.registerNewUser(
                    new UserId(authAcc.getAccountId()), // Important: UserId == AccountId!
                    userRegistrationRequestDTO.getUserProfileEmail(),
                    userRegistrationRequestDTO.getFirstName(),
                    userRegistrationRequestDTO.getLastName()
            );
            return ResponseEntity.ok(userApiMapper.toDTO(registeredUser));
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserSubscriptionDTO> subscribe(UUID userId, ExtendUserSubscriptionRequestDTO extendUserSubscriptionRequestDTO) {
        try {
            User user = userServicePort.extendUserSubscription(new UserId(userId), Duration.of(extendUserSubscriptionRequestDTO.getExtendByInDays(), ChronoUnit.DAYS));
            return ResponseEntity.ok(userApiMapper.toDTO(user).getSubscription());
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUserProfile(UUID userId, UpdateUserProfileRequestDTO updateUserProfileRequestDTO) {
        try {
            User updatedUser = userServicePort.updateUserProfile(
                    new UserId(userId),
                    new UserProfile(
                            updateUserProfileRequestDTO.getUserProfileEmail(),
                            updateUserProfileRequestDTO.getFirstName(),
                            updateUserProfileRequestDTO.getLastName()
                    ));
            return ResponseEntity.ok(userApiMapper.toDTO(updatedUser));
        } catch (EmailAlreadyInUseException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (UserNotFoundByIdException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUserSettings(UUID userId, UpdateUserSettingsRequestDTO updateUserSettingsRequestDTO) {
        try {
            User updatedUser = userServicePort.updateUserSettings(
                    new UserId(userId),
                    new UserSettings(updateUserSettingsRequestDTO.getNightModeActive()
                    ));
            return ResponseEntity.ok(userApiMapper.toDTO(updatedUser));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}

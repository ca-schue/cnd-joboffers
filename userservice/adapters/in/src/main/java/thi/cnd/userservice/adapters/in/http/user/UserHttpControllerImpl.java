package thi.cnd.userservice.adapters.in.http.user;

import jakarta.validation.ConstraintViolationException;
import thi.cnd.userservice.adapters.generated.rest.UserApi;
import thi.cnd.userservice.adapters.generated.rest.model.*;
import thi.cnd.userservice.adapters.in.http.jwtEndpointAuthentication.AuthenticatedAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import thi.cnd.userservice.domain.exceptions.*;
import thi.cnd.userservice.domain.model.company.*;
import thi.cnd.userservice.domain.model.user.*;
import thi.cnd.userservice.domain.UserService;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
class UserHttpControllerImpl implements UserApi {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Override
    public ResponseEntity<Void> acceptInvitation(UUID userId, AcceptCompanyInvitationRequestDTO requestDTO) {
        // Author. = Uid
        try {
            userService.acceptCompanyInvitation(new UserId(userId), new CompanyId(requestDTO.getCompanyId()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundByIdException | CompanyNotFoundByIdException e1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (UserAlreadyMemberOfCompanyException | UserNotInvitedException e2) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e2.getMessage());
        }
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        // Author. = Uid
        try {
            userService.deleteUser(new UserId(userId));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UserNotFoundByIdException | CompanyNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<PublicUserProfileDTO> getPublicUserProfile(UUID userId) {
        // Author. = offen
        try {
            User user = userService.findUserById(new UserId(userId));
            return ResponseEntity.ok(userDtoMapper.toPublicProfileDTO(user));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> getUser(UUID userId) {
        // Author. = Uid
        try {
            User user = userService.findUserById(new UserId(userId));
            return ResponseEntity.ok(userDtoMapper.toDTO(user));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> registerNewUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        // Author. = offen
        AuthenticatedAccount authAcc = (AuthenticatedAccount) SecurityContextHolder.getContext().getAuthentication();
        try {
            User registeredUser = userService.registerNewUser(
                    new UserId(authAcc.getAccountId()), // Important: UserId == AccountId!
                    userRegistrationRequestDTO.getUserProfileEmail(),
                    userRegistrationRequestDTO.getFirstName(),
                    userRegistrationRequestDTO.getLastName()
            );
            return ResponseEntity.ok(userDtoMapper.toDTO(registeredUser));
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<UserDTO> subscribe(UUID userId, ExtendUserSubscriptionRequestDTO extendUserSubscriptionRequestDTO) {
        // Author. = Uid
        try {
            User user = userService.extendUserSubscription(new UserId(userId), Duration.of(extendUserSubscriptionRequestDTO.getExtendByInDays(), ChronoUnit.DAYS));
            return ResponseEntity.ok(userDtoMapper.toDTO(user));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUserProfile(UUID userId, UpdateUserProfileRequestDTO updateUserProfileRequestDTO) {
        // Author. = Uid
        try {
            User updatedUser = userService.updateUserProfile(
                    new UserId(userId),
                    new UserProfile(
                            updateUserProfileRequestDTO.getUserProfileEmail(),
                            updateUserProfileRequestDTO.getFirstName(),
                            updateUserProfileRequestDTO.getLastName()
                    ));
            return ResponseEntity.ok(userDtoMapper.toDTO(updatedUser));
        } catch (EmailAlreadyInUseException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (UserNotFoundByIdException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (InvalidArgumentException e3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e3.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

    @Override
    public ResponseEntity<UserDTO> updateUserSettings(UUID userId, UpdateUserSettingsRequestDTO updateUserSettingsRequestDTO) {
        // Author. = Uid
        try {
            User updatedUser = userService.updateUserSettings(
                    new UserId(userId),
                    new UserSettings(updateUserSettingsRequestDTO.getNightModeActive()
                    ));
            return ResponseEntity.ok(userDtoMapper.toDTO(updatedUser));
        } catch (UserNotFoundByIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConstraintViolationException e4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter valid inputs.");
        }
    }

}

package thi.cnd.userservice.primary.rest;

import info.thale.userservice.api.generated.UserApi;
import info.thale.userservice.api.generated.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController implements UserApi {
    @Override
    public ResponseEntity<Void> acceptInvitation(UUID userId, AcceptCompanyInvitationRequestDTO acceptCompanyInvitationRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        return null;
    }

    @Override
    public ResponseEntity<PublicUserProfileDTO> getPublicUserProfile(UUID userId) {
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> getUser(UUID userId) {
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> registerNewUser(UserRegistrationRequestDTO userRegistrationRequestDTO) {
        // 1. get Authenticated email from JWT
        // register new user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }

    @Override
    public ResponseEntity<UserSubscriptionDTO> subscribe(UUID userId, ExtendUserSubscriptionRequestDTO extendUserSubscriptionRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> updateUserData(UUID userId, UpdateUserDataRequestDTO updateUserDataRequestDTO) {
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> updateUserProfileEmail(UUID userId, UpdateUserProfileEmailRequestDTO updateUserProfileEmailRequestDTO) {
        return null;
    }
}

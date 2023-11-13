package thi.cnd.userservice.domain.exceptions;

public class UserNotFoundByEmailException extends Exception {
    public UserNotFoundByEmailException(String userProfileEmail) {
        super("User with email " + userProfileEmail + " not found.");
    }
}

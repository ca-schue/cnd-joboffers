package thi.cnd.userservice.core.exception;

public class UserNotFoundByEmailException extends Exception {
    public UserNotFoundByEmailException(String userProfileEmail) {
        super("User with email " + userProfileEmail + " not found.");
    }
}

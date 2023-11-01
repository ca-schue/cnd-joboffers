package thi.cnd.userservice.core.exception;

public class UserAlreadyOwnerOfCompanyException extends Exception {
    public UserAlreadyOwnerOfCompanyException (String message) {
        super(message);
    }
}

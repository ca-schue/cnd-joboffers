package thi.cnd.userservice.domain.exceptions;

public class UserAlreadyOwnerOfCompanyException extends Exception {
    public UserAlreadyOwnerOfCompanyException (String message) {
        super(message);
    }
}

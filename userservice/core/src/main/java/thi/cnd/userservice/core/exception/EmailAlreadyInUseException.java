package thi.cnd.userservice.core.exception;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " already used by different user");
    }
}

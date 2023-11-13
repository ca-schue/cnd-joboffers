package thi.cnd.userservice.domain.exceptions;

public class EmailAlreadyInUseException extends Exception {
    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " already used by different user");
    }
}

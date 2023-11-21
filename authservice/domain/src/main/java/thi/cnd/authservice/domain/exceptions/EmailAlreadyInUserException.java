package thi.cnd.authservice.domain.exceptions;

public class EmailAlreadyInUserException extends Exception {
    public EmailAlreadyInUserException(String message) {
        super(message);
    }
}

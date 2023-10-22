package thi.cnd.authservice.core.exceptions;

public class EmailAlreadyInUserException extends Exception {
    public EmailAlreadyInUserException(String message) {
        super(message);
    }
}

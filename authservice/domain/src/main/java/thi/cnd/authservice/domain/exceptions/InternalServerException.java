package thi.cnd.authservice.domain.exceptions;

public class InternalServerException extends Exception{
    public InternalServerException (String message) {
        super(message);
    }
}

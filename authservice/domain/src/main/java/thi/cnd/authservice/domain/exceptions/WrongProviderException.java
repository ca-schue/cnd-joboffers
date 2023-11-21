package thi.cnd.authservice.domain.exceptions;

public class WrongProviderException extends Exception{
    public WrongProviderException(String message) {
        super(message);
    }
}

package thi.cnd.authservice.core.exceptions;

public class AccountStillVerifiedException extends Exception {
    public AccountStillVerifiedException(String message) {
        super(message);
    }
}

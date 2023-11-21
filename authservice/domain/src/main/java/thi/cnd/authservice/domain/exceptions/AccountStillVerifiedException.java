package thi.cnd.authservice.domain.exceptions;

public class AccountStillVerifiedException extends Exception {
    public AccountStillVerifiedException(String message) {
        super(message);
    }
}

package thi.cnd.authservice.core.exceptions;

public class AccountNotFoundByIdException extends Exception {
    public AccountNotFoundByIdException(String message) {
        super(message);
    }
}

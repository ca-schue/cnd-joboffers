package thi.cnd.authservice.domain.exceptions;

public class AccountNotFoundByIdException extends Exception {
    public AccountNotFoundByIdException(String message) {
        super(message);
    }
}

package thi.cnd.authservice.core.exceptions;

public class AccountNotFoundBySubjectException extends Exception {
    public AccountNotFoundBySubjectException(String message) {
        super(message);
    }
}

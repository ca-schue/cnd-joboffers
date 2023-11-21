package thi.cnd.authservice.domain.exceptions;

public class AccountNotFoundBySubjectException extends Exception {
    public AccountNotFoundBySubjectException(String message) {
        super(message);
    }
}

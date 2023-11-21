package thi.cnd.authservice.domain.exceptions;

public class AccountNotFoundByEmailException extends Exception{
    public AccountNotFoundByEmailException(String message) {
        super(message);
    }
}

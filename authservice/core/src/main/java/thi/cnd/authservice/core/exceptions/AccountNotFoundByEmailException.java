package thi.cnd.authservice.core.exceptions;

public class AccountNotFoundByEmailException extends Exception{
    public AccountNotFoundByEmailException(String message) {
        super(message);
    }
}

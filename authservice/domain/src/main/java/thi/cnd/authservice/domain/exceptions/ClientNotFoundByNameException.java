package thi.cnd.authservice.domain.exceptions;

public class ClientNotFoundByNameException extends Exception {
    public ClientNotFoundByNameException(String message) {
        super(message);
    }
}

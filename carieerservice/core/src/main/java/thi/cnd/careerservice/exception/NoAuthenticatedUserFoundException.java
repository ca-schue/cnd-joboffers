package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class NoAuthenticatedUserFoundException extends IdentifiedRuntimeException {

    public NoAuthenticatedUserFoundException() {
        super(HttpStatus.UNAUTHORIZED, () -> "User not authorized");
    }

}

package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputReceivedException extends IdentifiedRuntimeException {
    public InvalidInputReceivedException(String message) {
        super(HttpStatus.BAD_REQUEST, () -> message);
    }
}

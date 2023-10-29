package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends IdentifiedRuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, () -> message);
    }
}

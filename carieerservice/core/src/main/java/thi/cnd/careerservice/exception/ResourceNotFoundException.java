package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends IdentifiedRuntimeException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, () -> message);
    }
}

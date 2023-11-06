package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceIsDeletedException extends IdentifiedRuntimeException {
    public ResourceIsDeletedException() {
        super(HttpStatus.NOT_FOUND);
    }
}

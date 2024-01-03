package thi.cnd.careerservice.exception;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_FOUND;

public class ResourceNotFoundException extends IdentifiedRuntimeException {
    public ResourceNotFoundException(String message) {
        super(RESOURCE_NOT_FOUND, () -> message);
    }
}

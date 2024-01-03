package thi.cnd.careerservice.exception;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_ALREADY_EXISTS;

public class ResourceAlreadyExistsException extends IdentifiedRuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(RESOURCE_ALREADY_EXISTS, () -> message);
    }
}

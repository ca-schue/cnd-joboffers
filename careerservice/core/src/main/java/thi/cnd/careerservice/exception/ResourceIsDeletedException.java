package thi.cnd.careerservice.exception;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_NOT_FOUND;

public class ResourceIsDeletedException extends IdentifiedRuntimeException {
    public ResourceIsDeletedException() {
        super(RESOURCE_NOT_FOUND);
    }
}

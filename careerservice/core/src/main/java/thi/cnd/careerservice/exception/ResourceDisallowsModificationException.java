package thi.cnd.careerservice.exception;

import thi.cnd.careerservice.shared.model.BaseUUID;

import static thi.cnd.careerservice.exception.BasicErrorCode.RESOURCE_DISALLOWS_MODIFICATION;

public class ResourceDisallowsModificationException extends IdentifiedRuntimeException {
    public ResourceDisallowsModificationException(BaseUUID id) {
        super(RESOURCE_DISALLOWS_MODIFICATION, () -> "The resource with id " + id + " does not allow any modifications");
    }
}

package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

import thi.cnd.careerservice.shared.model.BaseUUID;

public class ResourceDisallowsModificationException extends IdentifiedRuntimeException {
    public ResourceDisallowsModificationException(BaseUUID id) {
        super(HttpStatus.CONFLICT, () -> "The resource with id " + id + " does not allow any modifications");
    }
}

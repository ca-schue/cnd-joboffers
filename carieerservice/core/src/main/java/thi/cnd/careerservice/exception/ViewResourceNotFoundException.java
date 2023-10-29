package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

import thi.cnd.careerservice.shared.model.BaseUUID;

public class ViewResourceNotFoundException extends IdentifiedRuntimeException {
    public ViewResourceNotFoundException(String viewName, BaseUUID id) {
        super(HttpStatus.NOT_FOUND, () -> "Could not find resource with id " + id + " in view " + viewName);
    }
    public ViewResourceNotFoundException(String viewName) {
        super(HttpStatus.NOT_FOUND, () -> "Could not find resource in view " + viewName);
    }
}

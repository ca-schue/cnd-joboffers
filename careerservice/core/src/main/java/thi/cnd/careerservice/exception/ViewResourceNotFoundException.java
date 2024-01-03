package thi.cnd.careerservice.exception;

import thi.cnd.careerservice.shared.model.BaseUUID;

import static thi.cnd.careerservice.exception.BasicErrorCode.VIEW_RESOURCE_NOT_FOUND;

public class ViewResourceNotFoundException extends IdentifiedRuntimeException {
    public ViewResourceNotFoundException(String viewName, BaseUUID id) {
        super(VIEW_RESOURCE_NOT_FOUND, () -> "Could not find resource with id " + id + " in view " + viewName);
    }
    public ViewResourceNotFoundException(String viewName) {
        super(VIEW_RESOURCE_NOT_FOUND, () -> "Could not find resource in view " + viewName);
    }
}

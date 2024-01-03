package thi.cnd.careerservice.exception;

import static thi.cnd.careerservice.exception.BasicErrorCode.GENERIC_INPUT_ERROR;

public class InvalidInputReceivedException extends IdentifiedRuntimeException {
    public InvalidInputReceivedException(String message) {
        super(GENERIC_INPUT_ERROR, () -> message);
    }
}

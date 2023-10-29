package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidETagHeaderException extends IdentifiedRuntimeException {
    public InvalidETagHeaderException(String headerName) {
        super(HttpStatus.PRECONDITION_REQUIRED, () -> headerName + " is missing or not a numeric value");
    }
}

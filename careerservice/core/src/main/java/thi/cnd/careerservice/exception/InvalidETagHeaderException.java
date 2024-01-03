package thi.cnd.careerservice.exception;

import static thi.cnd.careerservice.exception.BasicErrorCode.INVALID_ETAG_HEADER;

public class InvalidETagHeaderException extends IdentifiedRuntimeException {
    public InvalidETagHeaderException(String headerName) {
        super(INVALID_ETAG_HEADER, () -> headerName + " is missing or not a numeric value");
    }
}

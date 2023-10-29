package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DomainObjectInvalidArgumentException extends IdentifiedRuntimeException {

    public DomainObjectInvalidArgumentException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, () -> message);
    }

    public DomainObjectInvalidArgumentException(HttpStatusCode statusCode, String message) {
        super(statusCode, () -> message);
    }


}

package thi.cnd.careerservice.exception;

import java.util.function.Supplier;

import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class IdentifiedRuntimeException extends RuntimeException {

    private final HttpStatusCode status;

    public IdentifiedRuntimeException(HttpStatusCode status) {
        super();
        this.status = status;
    }

    public IdentifiedRuntimeException(Supplier<String> message) {
        super(message.get());
        this.status = INTERNAL_SERVER_ERROR;
    }

    public IdentifiedRuntimeException(Throwable e) {
        super(e.getMessage(), e);

        this.status = INTERNAL_SERVER_ERROR;
    }

    public IdentifiedRuntimeException(Supplier<String> message, Throwable e) {
        super(message.get(), e);

        this.status = INTERNAL_SERVER_ERROR;
    }

    public IdentifiedRuntimeException(HttpStatusCode status, Supplier<String> message) {
        super(message.get());

        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}

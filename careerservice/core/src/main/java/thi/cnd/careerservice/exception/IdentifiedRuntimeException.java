package thi.cnd.careerservice.exception;

import java.util.function.Supplier;

import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static thi.cnd.careerservice.exception.BasicErrorCode.UNKNOWN_ERROR;

@Getter
public class IdentifiedRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public IdentifiedRuntimeException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public IdentifiedRuntimeException(Supplier<String> message) {
        super(message.get());
        this.errorCode = UNKNOWN_ERROR;
    }

    public IdentifiedRuntimeException(Throwable e) {
        super(e.getMessage(), e);
        this.errorCode = UNKNOWN_ERROR;
    }

    public IdentifiedRuntimeException(Supplier<String> message, Throwable e) {
        super(message.get(), e);
        this.errorCode = UNKNOWN_ERROR;
    }

    public IdentifiedRuntimeException(ErrorCode errorCode, Supplier<String> message) {
        super(message.get());
        this.errorCode = errorCode;
    }

}

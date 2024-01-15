package thi.cnd.userservice.adapters.in.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpErrorException extends RuntimeException {
    private final int httpStatusCode;
    private final String httpStatusPhrase;
    private final String errorDetails;
    public HttpErrorException(HttpStatus httpStatus, String errorDetails) {
        this.httpStatusCode = httpStatus.value();
        this.httpStatusPhrase = httpStatus.getReasonPhrase();
        this.errorDetails = errorDetails;
    }
}

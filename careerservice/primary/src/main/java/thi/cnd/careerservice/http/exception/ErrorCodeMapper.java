package thi.cnd.careerservice.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import thi.cnd.careerservice.exception.BasicErrorCode;
import thi.cnd.careerservice.exception.ErrorCode;

public class ErrorCodeMapper {

    public static HttpStatusCode mapErrorCodeToHttpStatusCode(ErrorCode errorCode) {
        return switch (errorCode) {
            case BasicErrorCode basicErrorCode -> switch (basicErrorCode) {
                case UNKNOWN_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
                case RESOURCE_NOT_FOUND, VIEW_RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND;
                case RESOURCE_DISALLOWS_MODIFICATION, CONFLICTING_ACTION -> HttpStatus.CONFLICT;
                case RESOURCE_ALREADY_EXISTS, GENERIC_INPUT_ERROR -> HttpStatus.BAD_REQUEST;
                case INVALID_ETAG_HEADER, EXPECTED_REVISION_NOT_MATCHING -> HttpStatus.PRECONDITION_REQUIRED;
                case RESOURCE_NOT_MODIFIED -> HttpStatus.NOT_MODIFIED;
            };
        };
    }

}

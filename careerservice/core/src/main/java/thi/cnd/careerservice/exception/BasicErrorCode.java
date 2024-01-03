package thi.cnd.careerservice.exception;

public enum BasicErrorCode implements ErrorCode {

    UNKNOWN_ERROR(-1),
    RESOURCE_NOT_FOUND(1),
    VIEW_RESOURCE_NOT_FOUND(2),
    RESOURCE_DISALLOWS_MODIFICATION(3),
    RESOURCE_ALREADY_EXISTS(4),
    GENERIC_INPUT_ERROR(5),
    INVALID_ETAG_HEADER(6),
    EXPECTED_REVISION_NOT_MATCHING(7),
    RESOURCE_NOT_MODIFIED(8),
    CONFLICTING_ACTION(9);

    private final int errorCode;

    BasicErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}

package thi.cnd.careerservice.exception;

public sealed interface ErrorCode permits BasicErrorCode {
    int getErrorCode();
}

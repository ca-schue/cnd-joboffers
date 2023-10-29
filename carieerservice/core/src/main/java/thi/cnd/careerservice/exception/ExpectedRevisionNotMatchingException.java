package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

public class ExpectedRevisionNotMatchingException extends IdentifiedRuntimeException {

    public ExpectedRevisionNotMatchingException(long currentRevision, long expectedRevision) {
        super(HttpStatus.PRECONDITION_FAILED,
            () -> "The current revision " + currentRevision + " does not match the expected revision of " + expectedRevision);
    }

}

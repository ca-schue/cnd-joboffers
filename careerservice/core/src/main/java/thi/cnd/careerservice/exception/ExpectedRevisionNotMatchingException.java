package thi.cnd.careerservice.exception;

import org.springframework.http.HttpStatus;

import static thi.cnd.careerservice.exception.BasicErrorCode.EXPECTED_REVISION_NOT_MATCHING;

public class ExpectedRevisionNotMatchingException extends IdentifiedRuntimeException {

    public ExpectedRevisionNotMatchingException(long currentRevision, long expectedRevision) {
        super(EXPECTED_REVISION_NOT_MATCHING,
            () -> "The current revision " + currentRevision + " does not match the expected revision of " + expectedRevision);
    }

}

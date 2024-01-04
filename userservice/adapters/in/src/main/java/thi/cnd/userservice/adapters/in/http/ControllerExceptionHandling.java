package thi.cnd.userservice.adapters.in.http;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class ControllerExceptionHandling implements ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler
    public ResponseEntity<Problem> defaultExceptionHandler(Exception e) {
        return toResponseEntity(
            Problem.builder()
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withTitle("Unexpected Error")
                .withDetail(e.getMessage())
                .build()
        );
    }

    private ResponseEntity<Problem> toResponseEntity(ThrowableProblem problem) {
        var status = HttpStatus.valueOf(Objects.requireNonNull(problem.getStatus()).getStatusCode());
        return new ResponseEntity<>(problem, status);
    }

}

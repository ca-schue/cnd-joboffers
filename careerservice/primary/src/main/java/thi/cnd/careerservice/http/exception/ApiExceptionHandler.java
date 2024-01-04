package thi.cnd.careerservice.http.exception;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import thi.cnd.careerservice.exception.IdentifiedRuntimeException;

import static thi.cnd.careerservice.exception.BasicErrorCode.UNKNOWN_ERROR;


@ControllerAdvice
public class ApiExceptionHandler implements ProblemHandling {

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }

    @Bean
    public ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class, JwtException.class})
    public ResponseEntity<Void> handleUserNotFoundException() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {IdentifiedRuntimeException.class})
    public ResponseEntity<Problem> identifiedRuntimeExceptionHandler(IdentifiedRuntimeException e) {
        return toResponseEntity(
            Problem.builder()
                .withStatus(Status.valueOf(ErrorCodeMapper.mapErrorCodeToHttpStatusCode(e.getErrorCode()).value()))
                .withTitle(e.getErrorCode().getTitle())
                .withDetail(e.getMessage())
                .with("code", e.getErrorCode().getErrorCode())
                .build()
        );
    }

    @ExceptionHandler
    public ResponseEntity<Problem> defaultExceptionHandler(Exception e) {
        logger.error("Uncaught error occurred", e);

        return toResponseEntity(
            Problem.builder()
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withTitle(UNKNOWN_ERROR.getTitle())
                .with("code", UNKNOWN_ERROR.getErrorCode())
                .withDetail(e.getMessage())
                .build()
        );
    }

    private ResponseEntity<Problem> toResponseEntity(ThrowableProblem problem) {
        var status = HttpStatus.valueOf(Objects.requireNonNull(problem.getStatus()).getStatusCode());
        return new ResponseEntity<>(problem, status);
    }

}

package com.serendipity.common.error;

import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String ERRORS_PROPERTY = "errors";
    public static final String VALIDATION_FAILED_DETAIL = "Request validation failed";
    public static final String INTERNAL_ERROR_DETAIL = "Internal server error";
    public static final String TYPE_MISMATCH_MESSAGE_TEMPLATE = "must be a valid %s";

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception) {
        log.error(INTERNAL_ERROR_DETAIL, exception);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_DETAIL);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        return validationProblem(exception, errorViolations(exception.getBindingResult()), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<FieldViolation> violations = exception.getParameterValidationResults().stream()
                .flatMap(this::parameterViolations)
                .toList();
        return validationProblem(exception, violations, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        String field = exception instanceof MethodArgumentTypeMismatchException argumentException
                ? argumentException.getName()
                : exception.getPropertyName();
        Class<?> requiredType = exception.getRequiredType();
        String typeName = requiredType == null ? Object.class.getSimpleName() : requiredType.getSimpleName();
        FieldViolation violation = new FieldViolation(field, TYPE_MISMATCH_MESSAGE_TEMPLATE.formatted(typeName));
        return validationProblem(exception, List.of(violation), headers, status, request);
    }

    private Stream<FieldViolation> parameterViolations(ParameterValidationResult result) {
        if (result instanceof ParameterErrors errors) {
            return errorViolations(errors).stream();
        }
        String parameterName = result.getMethodParameter().getParameterName();
        return result.getResolvableErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .map(message -> new FieldViolation(parameterName, message));
    }

    private List<FieldViolation> errorViolations(Errors errors) {
        Stream<FieldViolation> fieldViolations = errors.getFieldErrors().stream()
                .map(error -> new FieldViolation(error.getField(), error.getDefaultMessage()));
        Stream<FieldViolation> objectViolations = errors.getGlobalErrors().stream()
                .map(error -> new FieldViolation(error.getObjectName(), error.getDefaultMessage()));
        return Stream.concat(fieldViolations, objectViolations).toList();
    }

    private ResponseEntity<Object> validationProblem(
            Exception exception,
            List<FieldViolation> violations,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, VALIDATION_FAILED_DETAIL);
        body.setProperty(ERRORS_PROPERTY, violations);
        return handleExceptionInternal(exception, body, headers, status, request);
    }
}

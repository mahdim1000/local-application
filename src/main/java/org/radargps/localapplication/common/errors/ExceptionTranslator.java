package org.radargps.localapplication.common.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.radargps.localapplication.common.errors.exception.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomErrorResponseException.class)
    public ResponseEntity<Object> handleCustomErrorResponse(CustomErrorResponseException ex, WebRequest request) {
        return createErrorResponse(ex.getBody(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraint(SQLIntegrityConstraintViolationException ex, WebRequest request) {
        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.SQL_INTEGRITY_CONSTRAINT_VIOLATION,
                ExceptionMessage.SQL_INTEGRITY_CONSTRAINT_VIOLATION.getKey(),
                ex.getMessage()
        );
        return createErrorResponse(errorResponse, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> violations = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.DATA_CONSTRAINT_VIOLATION,
                ExceptionMessage.DATA_CONSTRAINT_VIOLATION.getKey(),
                String.join(", ", violations)
        );
        return createErrorResponse(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format(
                "Parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(),
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Unknown"
        );

        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.INVALID_ARGUMENTS,
                ExceptionMessage.INVALID_ARGUMENT.getKey(),
                message
        );
        return createErrorResponse(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String message = "Invalid request content.";
        if (ex.getCause() instanceof InvalidFormatException cause) {
            if (cause.getTargetType().isEnum()) {
                message = String.format(
                        "Invalid value '%s' for enum '%s'. Allowed values are: %s",
                        cause.getValue(),
                        cause.getTargetType().getSimpleName(),
                        String.join(", ", getEnumConstants(cause.getTargetType()))
                );
            }
        }

        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.INVALID_INPUT_FORMAT_EXCEPTION,
                ExceptionMessage.INVALID_INPUT_FORMAT_EXCEPTION.getKey(),
                message
        );
        return createErrorResponse(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.INVALID_ARGUMENTS,
                ExceptionMessage.INVALID_ARGUMENT.getKey(),
                errors
        );
        return createErrorResponse(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse = createErrorResponse(
                ErrorCode.INVALID_ARGUMENTS,
                ExceptionMessage.INVALID_ARGUMENT.getKey(),
                "Required parameter '" + ex.getParameterName() + "' is missing"
        );
        return createErrorResponse(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    private ErrorResponse createErrorResponse(ErrorCode code, String message, Object details) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .details(details)
                .build();
    }

    private ResponseEntity<Object> createErrorResponse(Object body, HttpStatusCode status, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        String guid = java.util.UUID.randomUUID().toString();

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(path)
                .guid(guid)
                .error((ErrorResponse) body)
                .status(((HttpStatus) status).name())
                .statusCode(status.value())
                .timestamp(Instant.now().getEpochSecond())
                .build();

        return handleExceptionInternal(new RuntimeException(), apiErrorResponse, new HttpHeaders(), status, request);
    }

    private String[] getEnumConstants(Class<?> enumClass) {
        Object[] constants = enumClass.getEnumConstants();
        String[] names = new String[constants.length];
        for (int i = 0; i < constants.length; i++) {
            names[i] = constants[i].toString();
        }
        return names;
    }
}
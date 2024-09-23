package org.radargps.localapplication.common.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.radargps.localapplication.common.errors.exception.DataConstraintViolationException;
import org.radargps.localapplication.common.errors.exception.EntryAlreadyExistsException;
import org.radargps.localapplication.common.errors.exception.InvalidArgumentException;
import org.radargps.localapplication.common.errors.exception.InvalidInputFormatExceptionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomErrorResponseException.class)
    public ResponseEntity<Object> translate(CustomErrorResponseException ex, WebRequest webRequest) {
        return handleExceptionInternal(ex, ex.getBody(),
                new HttpHeaders(), ex.getHttpStatus(), webRequest);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> translate(SQLIntegrityConstraintViolationException ex, WebRequest webRequest) {
        var cex = new EntryAlreadyExistsException(ex.getMessage());

        if (ex.getMessage().toLowerCase().startsWith("duplicate entry")) {
            return handleExceptionInternal(ex, cex.getBody(), new HttpHeaders(),
                    cex.getHttpStatus(), webRequest);
        }
        return handleExceptionInternal(ex, cex.getBody(), new HttpHeaders(),
                cex.getHttpStatus(), webRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                     WebRequest webRequest) {
        var cex = new DataConstraintViolationException(ex.getMessage());
        return handleExceptionInternal(cex, cex.getBody(), new HttpHeaders(),
                cex.getHttpStatus(), webRequest);
    }
//    @ExceptionHandler(SystemException.class)
//    public ResponseEntity<Object> translate(SystemException ex, WebRequest webRequest) {
//        return handleExceptionInternal(ex, Map.of("errors",
//                        List.of(new BodyException(ex.getTitle(), ex.getMessage(), HttpStatus.BAD_REQUEST.value()))),
//                new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
//    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Object> translate(AccessDeniedException ex, WebRequest webRequest) {
//        return handleExceptionInternal(ex, Map.of("errors",
//                        List.of(new BodyException(ACCESS_DENIED_EXCEPTION, ex.getMessage(), 403))),
//                new HttpHeaders(), HttpStatus.FORBIDDEN, webRequest);
//    }


    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, WebRequest webRequest) {
        var cex = new InvalidInputFormatExceptionException(ex.getMessage());
        return handleExceptionInternal(cex, cex.getBody(), new HttpHeaders(),
                cex.getHttpStatus(), webRequest);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                               HttpStatusCode status, WebRequest webRequest) {
        BindingResult result = ex.getBindingResult();
//        List<BodyException> fieldErrors = result.getFieldErrors().stream()
//                .map(f -> new BodyException("INVALID_" + f.getField().toUpperCase(), f.getDefaultMessage(), 400))
//                .collect(Collectors.toList());
        String errorMessages = result.getFieldErrors().stream()
                .map(f -> "INVALID_" + f.getField().toUpperCase() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));


        var cex = new InvalidArgumentException(errorMessages);
        return handleExceptionInternal(cex, cex.getBody(), new HttpHeaders(),
                cex.getHttpStatus(), webRequest);
    }

//    @Override
//    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
//                                                        HttpStatusCode status, WebRequest request) {
//
//        return handleExceptionInternal(ex,
//                Map.of("errors", List.of(new BodyException("BAD_REQUEST", ex.getMessage(), 400))), new HttpHeaders(),
//                HttpStatus.BAD_REQUEST, request);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//                                                                  HttpHeaders headers, HttpStatusCode status,
//                                                                  WebRequest request) {
//        if (ex.getCause() instanceof InvalidFormatException) {
//            var message = "not valid type " + ((InvalidFormatException) ex.getCause()).getTargetType().getSimpleName();
//            return handleExceptionInternal(ex,
//                    Map.of("errors", List.of(new BodyException("BAD_REQUEST", message, 400))), new HttpHeaders(),
//                    HttpStatus.BAD_REQUEST, request);
//        }
//
//        return handleExceptionInternal(ex,
//                Map.of("errors", List.of(new BodyException("BAD_REQUEST", ex.getMessage(), 400))), new HttpHeaders(),
//                HttpStatus.BAD_REQUEST, request);
//
//    }
//
//    @Override
//    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
//                                                                       HttpHeaders headers, HttpStatusCode status,
//                                                                       WebRequest request) {
//        return handleExceptionInternal(ex,
//                Map.of("errors", List.of(new BodyException("BAD_REQUEST", ex.getMessage(), 400))), new HttpHeaders(),
//                HttpStatus.BAD_REQUEST, request);
//    }

}

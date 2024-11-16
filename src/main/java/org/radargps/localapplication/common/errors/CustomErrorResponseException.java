package org.radargps.localapplication.common.errors;

import org.springframework.http.HttpStatus;

public abstract class CustomErrorResponseException extends RuntimeException {
    protected final HttpStatus httpStatus;
    protected final ErrorCode code;
    protected final String message;
    protected final String details;
    protected final Body body;

    protected CustomErrorResponseException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        this.httpStatus = httpStatus;
        this.code = errorCode;
        this.message = message;
        this.details = "";
        body = new Body(errorCode, message, "");
    }

    protected CustomErrorResponseException(HttpStatus httpStatus, ErrorCode errorCode, String message, String details) {
        this.httpStatus = httpStatus;
        this.code = errorCode;
        this.message = message;
        this.details = details;
        body = new Body(errorCode, message, details);
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public Body getBody() {
        return this.body;
    }

    public record Body(ErrorCode code, String message, String details) {
    }
}



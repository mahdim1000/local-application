package org.radargps.localapplication.common.errors.exception;

import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class DataConstraintViolationException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public DataConstraintViolationException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.DATA_CONSTRAINT_VIOLATION,
                ExceptionMessage.DATA_CONSTRAINT_VIOLATION.getKey());
    }
    public DataConstraintViolationException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.DATA_CONSTRAINT_VIOLATION,
                message.getKey());
    }

    public DataConstraintViolationException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.DATA_CONSTRAINT_VIOLATION,
                ExceptionMessage.DATA_CONSTRAINT_VIOLATION.getKey(),
                details);
    }

    public DataConstraintViolationException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.DATA_CONSTRAINT_VIOLATION,
                message.getKey(),
                details);
    }

}

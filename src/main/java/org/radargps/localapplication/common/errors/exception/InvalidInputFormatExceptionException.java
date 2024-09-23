package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class InvalidInputFormatExceptionException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public InvalidInputFormatExceptionException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_INPUT_FORMAT_EXCEPTION,
                ExceptionMessage.INVALID_INPUT_FORMAT_EXCEPTION.getKey());
    }
    public InvalidInputFormatExceptionException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_INPUT_FORMAT_EXCEPTION,
                message.getKey());
    }

    public InvalidInputFormatExceptionException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_INPUT_FORMAT_EXCEPTION,
                ExceptionMessage.INVALID_INPUT_FORMAT_EXCEPTION.getKey(),
                details);
    }

    public InvalidInputFormatExceptionException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_INPUT_FORMAT_EXCEPTION,
                message.getKey(),
                details);
    }

}

package org.radargps.localapplication.common.errors.exception;



import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public InvalidArgumentException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS,
                ExceptionMessage.INVALID_ARGUMENT.getKey());
    }
    public InvalidArgumentException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS, message.getKey());
    }

    public InvalidArgumentException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS,
                ExceptionMessage.INVALID_ARGUMENT.getKey(),
                details);
    }

    public InvalidArgumentException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS, message.getKey(), details);
    }

}

package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class InvalidPaginationParametersException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public InvalidPaginationParametersException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAGINATION_PARAMETERS,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey());
    }
    public InvalidPaginationParametersException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAGINATION_PARAMETERS,
                message.getKey());
    }

    public InvalidPaginationParametersException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAGINATION_PARAMETERS,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey(),
                details);
    }

    public InvalidPaginationParametersException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAGINATION_PARAMETERS,
                message.getKey(),
                details);
    }

}

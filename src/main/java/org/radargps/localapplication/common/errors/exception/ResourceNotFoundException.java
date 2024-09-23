package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.RESOURCE_NOT_FOUND,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey());
    }
    public ResourceNotFoundException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.RESOURCE_NOT_FOUND,
                message.getKey());
    }

    public ResourceNotFoundException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.RESOURCE_NOT_FOUND,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey(),
                details);
    }

    public ResourceNotFoundException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.RESOURCE_NOT_FOUND,
                message.getKey(),
                details);
    }

}

package org.radargps.localapplication.common.errors.exception;

import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class AlreadyAssignedException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public AlreadyAssignedException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.ALREADY_ASSIGNED,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey());
    }
    public AlreadyAssignedException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.ALREADY_ASSIGNED,
                message.getKey());
    }

    public AlreadyAssignedException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.ALREADY_ASSIGNED,
                ExceptionMessage.RESOURCE_NOT_FOUND.getKey(),
                details);
    }

    public AlreadyAssignedException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.ALREADY_ASSIGNED,
                message.getKey(),
                details);
    }

}

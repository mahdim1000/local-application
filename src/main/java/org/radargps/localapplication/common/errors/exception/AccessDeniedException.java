package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public AccessDeniedException() {
        super(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, ExceptionMessage.ACCESS_DENIED.getKey());
    }
    public AccessDeniedException(ExceptionMessage message) {
        super(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, message.getKey());
    }

}

package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class BadRequestAlertException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public BadRequestAlertException(ErrorCode code, ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST, code, message.getKey());
    }

}

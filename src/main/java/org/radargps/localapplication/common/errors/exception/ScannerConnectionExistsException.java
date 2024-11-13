package org.radargps.localapplication.common.errors.exception;

import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class ScannerConnectionExistsException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public ScannerConnectionExistsException() {
        super(HttpStatus.CONFLICT,
                ErrorCode.ACTIVE_CONNECTION_EXISTS,
                ExceptionMessage.SCANNER_HAS_ACTIVE_CONNECTION.getKey());
    }

    public ScannerConnectionExistsException(String details) {
        super(HttpStatus.CONFLICT,
                ErrorCode.ACTIVE_CONNECTION_EXISTS,
                ExceptionMessage.SCANNER_HAS_ACTIVE_CONNECTION.getKey(),
                details);
    }

    public ScannerConnectionExistsException(ExceptionMessage message) {
        super(HttpStatus.CONFLICT,
                ErrorCode.ACTIVE_CONNECTION_EXISTS,
                message.getKey());
    }

    public ScannerConnectionExistsException(ExceptionMessage message, String details) {
        super(HttpStatus.CONFLICT,
                ErrorCode.ACTIVE_CONNECTION_EXISTS,
                message.getKey(),
                details);
    }
}
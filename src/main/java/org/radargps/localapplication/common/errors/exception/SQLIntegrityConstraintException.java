package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class SQLIntegrityConstraintException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public SQLIntegrityConstraintException() {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.SQL_INTEGRITY_CONSTRAINT_VIOLATION,
                ExceptionMessage.SQL_INTEGRITY_CONSTRAINT_VIOLATION.getKey());
    }
    public SQLIntegrityConstraintException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.SQL_INTEGRITY_CONSTRAINT_VIOLATION,
                message.getKey());
    }

    public SQLIntegrityConstraintException(String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.SQL_INTEGRITY_CONSTRAINT_VIOLATION,
                ExceptionMessage.SQL_INTEGRITY_CONSTRAINT_VIOLATION.getKey(),
                details);
    }

    public SQLIntegrityConstraintException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST,
                ErrorCode.RESOURCE_NOT_FOUND,
                message.getKey(),
                details);
    }

}

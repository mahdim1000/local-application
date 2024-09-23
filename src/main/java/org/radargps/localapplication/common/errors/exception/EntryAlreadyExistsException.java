package org.radargps.localapplication.common.errors.exception;


import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class EntryAlreadyExistsException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public EntryAlreadyExistsException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ENTRY_ALREADY_EXISTS, ExceptionMessage.EXCEL_PARSING_FAILED.getKey());
    }
    public EntryAlreadyExistsException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ENTRY_ALREADY_EXISTS, message.getKey());
    }

    public EntryAlreadyExistsException(String details) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ENTRY_ALREADY_EXISTS,
                ExceptionMessage.EXCEL_PARSING_FAILED.getKey(),
                details);
    }

    public EntryAlreadyExistsException(ExceptionMessage message, String details) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.ENTRY_ALREADY_EXISTS, message.getKey(), details);
    }

}

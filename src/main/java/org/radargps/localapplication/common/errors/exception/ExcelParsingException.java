package org.radargps.localapplication.common.errors.exception;



import org.radargps.localapplication.common.errors.CustomErrorResponseException;
import org.radargps.localapplication.common.errors.ErrorCode;
import org.radargps.localapplication.common.errors.ExceptionMessage;
import org.springframework.http.HttpStatus;

public class ExcelParsingException extends CustomErrorResponseException {
    private static final long serialVersionUID = 1L;

    public ExcelParsingException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.EXCEL_PARSING_FAILED, ExceptionMessage.EXCEL_PARSING_FAILED.getKey());
    }
    public ExcelParsingException(ExceptionMessage message) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.EXCEL_PARSING_FAILED, message.getKey());
    }

    public ExcelParsingException(String details) {
        super(HttpStatus.BAD_REQUEST, ErrorCode.EXCEL_PARSING_FAILED,
                ExceptionMessage.EXCEL_PARSING_FAILED.getKey(),
                details);
    }

}

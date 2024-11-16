package org.radargps.localapplication.common.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private String path;
    private String guid;
    private ErrorResponse error;
    private String status;
    private int statusCode;
    private long timestamp;
}
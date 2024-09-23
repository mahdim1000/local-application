package org.radargps.localapplication.common.errors;

public enum ExceptionMessage {
    ACCESS_DENIED("Access not permitted"),

    USER_BLOCKED("User blocked"),

    EXCEL_PARSING_FAILED("Failed to parse the Excel file"),

    FIELD_ALREADY_EXISTS("Field already exists"),
    NAME_FIELD_ALREADY_EXISTS("Name already exists"),
    UNIQUE_ID_FIELD_ALREADY_EXISTS("UniqueId already exists"),
    MAP_ID_FIELD_ALREADY_EXISTS("MapId already exists"),

    INVALID_ARGUMENT("Argument provided is invalid"),
    UNIQUE_ID_INVALID_ARGUMENT("UniqueId is invalid"),
    COMPANY_ID_INVALID_ARGUMENT("CompanyId is invalid"),
    IDENTIFIER_INVALID_ARGUMENT("Identifier is invalid"),
    NAME_INVALID_ARGUMENT("Name is invalid"),
    GEOFENCE_TYPE_INVALID_ARGUMENT("Geofence type is invalid"),
    GEOFENCE_GEOMETRY_INVALID_ARGUMENT("Geofence geometry is invalid"),
    MAP_ID_INVALID_ARGUMENT("MapId is invalid"),

    RESOURCE_NOT_FOUND("Resource not found"),

    INVALID_PAGINATION_PARAMETERS("Pagination parameters is invalid"),
    PAGE_SIZE_INVALID_PAGINATION_PARAMETERS("PageSize parameter is invalid"),
    PAGE_NUMBER_INVALID_PAGINATION_PARAMETERS("PageNumber parameter is invalid"),
    SORT_INVALID_PAGINATION_PARAMETERS("Sort parameter is invalid"),

    ALREADY_ASSIGNED("Already assigned"),
    DEVICE_ALREADY_ASSIGNED("Device Already assigned"),
    ENTITY_ALREADY_ASSIGNED("Entity already assigned"),

    SQL_INTEGRITY_CONSTRAINT_VIOLATION("Constraint violation. Unable to process your request"),

    DATA_CONSTRAINT_VIOLATION("Constraint violation. Please correct your data"),

    INVALID_INPUT_FORMAT_EXCEPTION("Incorrect input format");

    private final String key;

    ExceptionMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

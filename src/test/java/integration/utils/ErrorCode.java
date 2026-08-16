package integration.utils;

public enum ErrorCode {

    DISK_NOT_FOUND("DiskNotFoundError"),
    FieldValidationError("FieldValidationError"),
    DiskPathPointsToExistentDirectoryError("DiskPathPointsToExistentDirectoryError"),
    MethodNotAllowedError("MethodNotAllowedError"),
    DiskPathDoesntExistsError("DiskPathDoesntExistsError"),
    DiskResourceAlreadyExistsError("DiskResourceAlreadyExistsError");


    private final String code;

    ErrorCode(String code) { this.code = code; }
    public String getCode() { return code; }
}

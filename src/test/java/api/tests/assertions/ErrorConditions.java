package api.tests.assertions;


import api.tests.dto.ErrorResponse;
import api.utils.ErrorCode;
import org.assertj.core.api.Condition;

public class ErrorConditions {

    public static Condition<ErrorResponse> withError(ErrorCode expectedCode) {
        return new Condition<>(
                response -> response.getError().equals(expectedCode.getCode()),
                "с кодом ошибки '%s'", expectedCode.getCode()
        );
    }
    public static Condition<ErrorResponse> diskNotFound() {
        return withError(ErrorCode.DISK_NOT_FOUND);
    }

    public static Condition<ErrorResponse> FieldValidationError() {
        return withError(ErrorCode.FieldValidationError);
    }

    public static Condition<ErrorResponse> DiskPathPointsToExistentDirectoryError() {
        return withError(ErrorCode.DiskPathPointsToExistentDirectoryError);
    }

    public static Condition<ErrorResponse> DiskPathDoesntExistsError(){
        return withError(ErrorCode.DiskPathDoesntExistsError);
    }
    public static Condition<ErrorResponse> MethodNotAllowedError(){
        return withError(ErrorCode.MethodNotAllowedError);
    }

    public static Condition<ErrorResponse> conflict() {
        return withError(ErrorCode.DiskResourceAlreadyExistsError);
    }
}

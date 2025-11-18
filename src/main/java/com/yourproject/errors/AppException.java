package com.yourproject.errors;

import java.util.Map;

public abstract class AppException extends RuntimeException {
    private final ErrorCode errorCode;
    private final int httpStatus;
    private final Map<String, Object> details;

    public AppException(
        ErrorCode errorCode,
        String message,
        int httpStatus,
        Map<String, Object> details
    ) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public AppException(
        ErrorCode errorCode,
        String message,
        int httpStatus
    ) {
        this(errorCode, message, httpStatus, null);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}

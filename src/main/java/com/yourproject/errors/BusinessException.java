package com.yourproject.errors;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class BusinessException extends AppException {
    public BusinessException(String message) {
        super(
            ErrorCode.BUSINESS_ERROR,
            message,
            HttpStatus.BAD_REQUEST.value()
        );
    }

    public BusinessException(String message, Map<String, Object> details) {
        super(
            ErrorCode.BUSINESS_ERROR,
            message,
            HttpStatus.BAD_REQUEST.value(),
            details
        );
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(
            errorCode,
            message,
            HttpStatus.BAD_REQUEST.value()
        );
    }

    public BusinessException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(
            errorCode,
            message,
            HttpStatus.BAD_REQUEST.value(),
            details
        );
    }
}

package com.yourproject.errors;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(
            ErrorCode.NOT_FOUND,
            message,
            HttpStatus.NOT_FOUND.value()
        );
    }

    public NotFoundException(String message, Map<String, Object> details) {
        super(
            ErrorCode.NOT_FOUND,
            message,
            HttpStatus.NOT_FOUND.value(),
            details
        );
    }
}

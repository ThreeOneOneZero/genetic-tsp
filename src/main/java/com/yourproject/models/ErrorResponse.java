package com.yourproject.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    boolean success,
    ErrorDetail error,
    String timestamp
) {
    public record ErrorDetail(
        String code,
        String message,
        Map<String, Object> details
    ) {
        public static ErrorDetail of(String code, String message) {
            return new ErrorDetail(code, message, null);
        }

        public static ErrorDetail of(String code, String message, Map<String, Object> details) {
            return new ErrorDetail(code, message, details);
        }
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(
            false,
            ErrorDetail.of(code, message),
            Instant.now().toString()
        );
    }

    public static ErrorResponse of(String code, String message, Map<String, Object> details) {
        return new ErrorResponse(
            false,
            ErrorDetail.of(code, message, details),
            Instant.now().toString()
        );
    }
}

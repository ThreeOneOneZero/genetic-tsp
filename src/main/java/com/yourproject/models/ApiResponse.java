package com.yourproject.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    T data,
    String timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            true,
            data,
            Instant.now().toString()
        );
    }

    public static <T> ApiResponse<T> empty() {
        return new ApiResponse<>(
            true,
            null,
            Instant.now().toString()
        );
    }
}

package com.yourproject.controllers;

import com.yourproject.models.ApiResponse;
import com.yourproject.models.ErrorResponse;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    protected ResponseEntity<ApiResponse<Void>> okEmpty() {
        return ResponseEntity.ok(ApiResponse.empty());
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(201).body(ApiResponse.success(data));
    }

    protected ResponseEntity<ErrorResponse> error(ErrorResponse errorResponse) {
        return ResponseEntity
            .status(500)
            .body(errorResponse);
    }
}

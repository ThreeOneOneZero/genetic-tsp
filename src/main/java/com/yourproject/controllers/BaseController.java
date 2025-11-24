package com.yourproject.controllers;

import com.yourproject.models.ApiResponse;
import com.yourproject.models.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
    org.springframework.web.bind.annotation.RequestMethod.GET,
    org.springframework.web.bind.annotation.RequestMethod.POST,
    org.springframework.web.bind.annotation.RequestMethod.PUT,
    org.springframework.web.bind.annotation.RequestMethod.DELETE,
    org.springframework.web.bind.annotation.RequestMethod.OPTIONS,
    org.springframework.web.bind.annotation.RequestMethod.PATCH
})
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

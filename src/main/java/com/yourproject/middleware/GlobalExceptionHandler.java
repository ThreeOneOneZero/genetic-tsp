package com.yourproject.middleware;

import com.yourproject.errors.AppException;
import com.yourproject.errors.ErrorCode;
import com.yourproject.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
        AppException ex,
        WebRequest request
    ) {
        logger.warn("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
            ex.getErrorCode().getCode(),
            ex.getMessage(),
            ex.getDetails()
        );

        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        WebRequest request
    ) {
        logger.warn("Validation error: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            details.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = ErrorResponse.of(
            ErrorCode.INVALID_INPUT.getCode(),
            "Validation failed",
            details
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
        IllegalArgumentException ex,
        WebRequest request
    ) {
        logger.warn("Invalid argument: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
            ErrorCode.INVALID_INPUT.getCode(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
        IllegalStateException ex,
        WebRequest request
    ) {
        logger.warn("Invalid state: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
            ErrorCode.BUSINESS_ERROR.getCode(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
        HttpRequestMethodNotSupportedException ex,
        WebRequest request
    ) {
        String message = String.format("Method %s not supported for this endpoint. Supported methods: %s",
            ex.getMethod(),
            String.join(", ", ex.getSupportedMethods() != null ? ex.getSupportedMethods() : new String[]{"Unknown"}));
        
        logger.warn("Method not supported: {}", message);

        ErrorResponse response = ErrorResponse.of(
            "METHOD_NOT_ALLOWED",
            message
        );

        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        WebRequest request
    ) {
        logger.error("Unexpected exception: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.of(
            ErrorCode.INTERNAL_ERROR.getCode(),
            "Internal server error"
        );

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}

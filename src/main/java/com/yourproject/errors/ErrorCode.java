package com.yourproject.errors;

public enum ErrorCode {
    INTERNAL_ERROR("INTERNAL_ERROR"),
    NOT_FOUND("NOT_FOUND"),
    INVALID_INPUT("INVALID_INPUT"),
    UNAUTHORIZED("UNAUTHORIZED"),
    FORBIDDEN("FORBIDDEN"),
    CONFLICT("CONFLICT"),
    BUSINESS_ERROR("BUSINESS_ERROR");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

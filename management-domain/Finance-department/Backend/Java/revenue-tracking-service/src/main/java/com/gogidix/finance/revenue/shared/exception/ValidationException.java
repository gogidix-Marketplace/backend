package com.gogidix.finance.revenue.shared.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) { super(message); }
    public ValidationException(String field, String reason) {
        super("Validation failed for '" + field + "': " + reason);
    }

    private String code;

    public String getErrorCode() {
        return code;
    }

    public void setErrorCode(String code) {
        this.code = code;
    }

}
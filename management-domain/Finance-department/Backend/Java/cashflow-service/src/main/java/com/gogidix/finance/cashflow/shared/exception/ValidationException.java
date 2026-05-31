package com.gogidix.finance.cashflow.shared.exception;

public class ValidationException extends RuntimeException {

    private String code;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

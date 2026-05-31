package com.gogidix.sales.dealmanagement.shared.exception;

public class ValidationException extends RuntimeException {

    private String field;
    private String code;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

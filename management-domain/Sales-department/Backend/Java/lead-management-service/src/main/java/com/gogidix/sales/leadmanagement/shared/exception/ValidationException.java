package com.gogidix.sales.leadmanagement.shared.exception;

public class ValidationException extends RuntimeException {

    private String code;
    private String field;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public ValidationException(String message, String code, String field) {
        super(message);
        this.code = code;
        this.field = field;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
}

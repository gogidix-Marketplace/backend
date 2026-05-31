package com.gogidix.sales.territory.shared.exception;

public class ValidationException extends RuntimeException {

    private String code;
    private String field;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String code) {
        super(message);
        this.code = code;
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

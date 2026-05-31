package com.gogidix.sales.communication.shared.exception;

public class BusinessRuleException extends RuntimeException {

    private String code;

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

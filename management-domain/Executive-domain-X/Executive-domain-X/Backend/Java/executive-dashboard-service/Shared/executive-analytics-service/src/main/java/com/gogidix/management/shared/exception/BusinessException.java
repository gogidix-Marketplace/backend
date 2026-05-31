package com.gogidix.management.shared.exception;

public class BusinessException extends DomainException {
    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR");
    }

    public BusinessException(String code, String message) {
        super(message, code);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

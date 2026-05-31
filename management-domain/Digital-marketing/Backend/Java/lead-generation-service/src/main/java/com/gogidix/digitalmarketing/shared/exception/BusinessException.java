package com.gogidix.digitalmarketing.shared.exception;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessException extends DomainException {

    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_ERROR", message, cause);
    }
}

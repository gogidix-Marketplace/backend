package com.gogidix.aiservices.aigatewayservice.shared.exception;

/**
 * Exception thrown when a business rule is violated.
 */
public class BusinessException extends BaseDomainException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    protected String deriveErrorCode() {
        return "BUSINESS_RULE_VIOLATION";
    }
}

package com.gogidix.shared.exceptions.domain.exception;

/**
 * Business rule exception for business logic violations
 */
public class BusinessRuleException extends BusinessException {

    public BusinessRuleException(String message) {
        super(message, "BUSINESS_RULE_VIOLATION", 400);
    }

    public BusinessRuleException(String message, String errorCode) {
        super(message, errorCode, 400);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause, "BUSINESS_RULE_VIOLATION", 400);
    }
}

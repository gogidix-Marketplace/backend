package com.gogidix.aiservices.intelligenceanalysisservice.shared.exception;

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

    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }

    @Override
    protected String deriveErrorCode() {
        return "BUSINESS_RULE_VIOLATION";
    }
}

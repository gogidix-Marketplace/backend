package com.gogidix.shared.exceptions.domain.exception;

/**
 * Business logic exceptions (4xx errors)
 * Thrown when business rules are violated
 */
public class BusinessException extends BaseException {
    
    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR", 400, "BUSINESS");
    }
    
    public BusinessException(String message, String errorCode) {
        super(message, errorCode, 400, "BUSINESS");
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause, "BUSINESS_ERROR", 400, "BUSINESS");
    }
    
    public BusinessException(String message, String errorCode, int httpStatus) {
        super(message, errorCode, httpStatus, "BUSINESS");
    }

    public BusinessException(String message, Throwable cause, String errorCode, int httpStatus) {
        super(message, cause, errorCode, httpStatus, "BUSINESS");
    }
}

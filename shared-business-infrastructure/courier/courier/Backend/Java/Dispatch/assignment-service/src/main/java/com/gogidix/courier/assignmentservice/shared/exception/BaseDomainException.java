package com.gogidix.courier.assignmentservice.shared.exception;

/**
 * Base exception for all domain exceptions.
 */
public abstract class BaseDomainException extends RuntimeException {

    private final String errorCode;

    public BaseDomainException(String message) {
        super(message);
        this.errorCode = deriveErrorCode();
    }

    public BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = deriveErrorCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    protected abstract String deriveErrorCode();
}

package com.gogidix.courier.availabilityservice.shared.exception;

/**
 * Base exception for domain-related errors.
 */
public abstract class BaseDomainException extends RuntimeException {

    private final String errorCode;

    protected BaseDomainException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    protected BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    public String getErrorCode() {
        return errorCode;
    }
}

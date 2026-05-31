package com.gogidix.courier.routingservice.shared.exception;

/**
 * Base exception for all domain-related errors.
 */
public abstract class BaseDomainException extends RuntimeException {

    public BaseDomainException(String message) {
        super(message);
    }

    public BaseDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Get the error code for this exception.
     *
     * @return the error code
     */
    protected abstract String deriveErrorCode();

    /**
     * Get the error code.
     *
     * @return the error code
     */
    public final String getErrorCode() {
        String errorCode = deriveErrorCode();
        return errorCode != null ? errorCode : "UNKNOWN_ERROR";
    }
}

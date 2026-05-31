package com.gogidix.courier.etaservice.shared.exception;

/**
 * Base exception for all domain exceptions.
 */
public abstract class BaseDomainException extends RuntimeException {

    private final String errorCode;

    protected BaseDomainException(String message) {
        super(message);
        this.errorCode = deriveErrorCode();
    }

    protected BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = deriveErrorCode();
    }

    /**
     * Derive the error code for this exception.
     * Subclasses should override this method to provide specific error codes.
     *
     * @return the error code
     */
    protected abstract String deriveErrorCode();

    /**
     * Get the error code for this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}

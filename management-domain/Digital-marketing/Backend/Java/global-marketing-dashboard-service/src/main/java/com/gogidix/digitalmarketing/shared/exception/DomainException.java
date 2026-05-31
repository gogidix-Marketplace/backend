package com.gogidix.digitalmarketing.shared.exception;

/**
 * Base class for all domain exceptions.
 *
 * <p>All custom exceptions should extend this class to ensure consistent error handling.</p>
 */
public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    public DomainException(String message) {
        super(message);
        this.errorCode = this.getClass().getSimpleName();
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = this.getClass().getSimpleName();
    }

    public DomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

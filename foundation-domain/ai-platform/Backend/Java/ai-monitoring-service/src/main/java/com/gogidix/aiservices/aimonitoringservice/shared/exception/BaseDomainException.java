package com.gogidix.aiservices.aimonitoringservice.shared.exception;

/**
 * Base exception for all domain-specific exceptions.
 */
public abstract class BaseDomainException extends RuntimeException {

    private final String errorCode;

    protected BaseDomainException(String message) {
        this(message, (Throwable) null);
    }

    protected BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = deriveErrorCode();
    }

    protected BaseDomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected BaseDomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    protected String deriveErrorCode() {
        String simpleName = this.getClass().getSimpleName();
        if (simpleName.endsWith("Exception")) {
            simpleName = simpleName.substring(0, simpleName.length() - "Exception".length());
        }
        return simpleName.toUpperCase();
    }

    public String getErrorCode() {
        return errorCode;
    }
}

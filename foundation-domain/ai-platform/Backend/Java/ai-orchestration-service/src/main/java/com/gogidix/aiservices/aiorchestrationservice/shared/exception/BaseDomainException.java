package com.gogidix.aiservices.aiorchestrationservice.shared.exception;

public abstract class BaseDomainException extends RuntimeException {
    private final String errorCode;

    protected BaseDomainException(String message) {
        this(message, (Throwable) null);
    }

    protected BaseDomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = deriveErrorCode();
    }

    protected String deriveErrorCode() {
        String simpleName = this.getClass().getSimpleName();
        if (simpleName.endsWith("Exception")) {
            simpleName = simpleName.substring(0, simpleName.length() - "Exception".length());
        }
        return simpleName.toUpperCase();
    }

    public String getErrorCode() { return errorCode; }
}

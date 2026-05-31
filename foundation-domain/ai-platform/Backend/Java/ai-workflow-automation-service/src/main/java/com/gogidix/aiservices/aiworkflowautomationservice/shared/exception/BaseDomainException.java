package com.gogidix.aiservices.aiworkflowautomationservice.shared.exception;
public abstract class BaseDomainException extends RuntimeException {
    private final String errorCode;
    protected BaseDomainException(String message) { this(message, null); }
    protected BaseDomainException(String message, Throwable cause) { super(message, cause); this.errorCode = deriveErrorCode(); }
    protected String deriveErrorCode() { return getClass().getSimpleName().replace("Exception", "").toUpperCase(); }
    public String getErrorCode() { return errorCode; }
}

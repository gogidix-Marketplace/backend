package com.gogidix.aiservices.aiorchestrationservice.shared.exception;

public class ValidationException extends BaseDomainException {
    public ValidationException(String message) { super(message); }

    @Override
    protected String deriveErrorCode() { return "VALIDATION_ERROR"; }
}

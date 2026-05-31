package com.gogidix.aiservices.aimonitoringservice.shared.exception;

/**
 * Exception thrown when a conflict occurs.
 */
public class ConflictException extends BaseDomainException {

    public ConflictException(String message) {
        super(message);
    }

    @Override
    protected String deriveErrorCode() {
        return "CONFLICT";
    }
}

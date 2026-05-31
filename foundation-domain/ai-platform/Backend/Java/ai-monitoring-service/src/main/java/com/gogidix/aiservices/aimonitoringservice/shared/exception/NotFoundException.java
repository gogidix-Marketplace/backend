package com.gogidix.aiservices.aimonitoringservice.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends BaseDomainException {

    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' was not found", resourceType, resourceId));
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    protected String deriveErrorCode() {
        return "RESOURCE_NOT_FOUND";
    }
}

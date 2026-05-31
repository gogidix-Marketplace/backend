package com.gogidix.courier.routingservice.shared.exception;

/**
 * Exception thrown when a conflict occurs (e.g., duplicate resource).
 */
public class ConflictException extends BaseDomainException {

    private final String resourceType;
    private final String resourceId;

    public ConflictException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' already exists", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ConflictException(String message) {
        super(message);
        this.resourceType = "RESOURCE";
        this.resourceId = "UNKNOWN";
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
        this.resourceType = "RESOURCE";
        this.resourceId = "UNKNOWN";
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    @Override
    protected String deriveErrorCode() {
        return "CONFLICT_ERROR";
    }
}

package com.gogidix.courier.gpstrackingservice.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends BaseDomainException {

    private final String resourceType;
    private final String resourceId;

    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' not found", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public NotFoundException(String resourceType, String resourceId, Throwable cause) {
        super(String.format("%s with id '%s' not found", resourceType, resourceId), cause);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public NotFoundException(String message) {
        super(message);
        this.resourceType = "Unknown";
        this.resourceId = "Unknown";
    }

    @Override
    protected String deriveErrorCode() {
        return "NOT_FOUND";
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}

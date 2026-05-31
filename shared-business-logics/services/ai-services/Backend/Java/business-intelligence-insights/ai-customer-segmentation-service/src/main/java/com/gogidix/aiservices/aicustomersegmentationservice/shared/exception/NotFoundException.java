package com.gogidix.aiservices.aicustomersegmentationservice.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends BaseDomainException {

    private final String resourceType;
    private final String resourceId;

    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' was not found", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public NotFoundException(String message) {
        super(message);
        this.resourceType = "RESOURCE";
        this.resourceId = "UNKNOWN";
    }

    public NotFoundException(String message, Throwable cause) {
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
        return "RESOURCE_NOT_FOUND";
    }
}

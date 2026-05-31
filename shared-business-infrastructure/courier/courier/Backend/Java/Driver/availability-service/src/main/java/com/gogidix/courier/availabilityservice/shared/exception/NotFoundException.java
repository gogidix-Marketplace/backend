package com.gogidix.courier.availabilityservice.shared.exception;

/**
 * Exception thrown when a requested resource is not found.
 */
public class NotFoundException extends BaseDomainException {

    private final String resourceType;
    private final String resourceId;

    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with ID '%s' was not found", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}

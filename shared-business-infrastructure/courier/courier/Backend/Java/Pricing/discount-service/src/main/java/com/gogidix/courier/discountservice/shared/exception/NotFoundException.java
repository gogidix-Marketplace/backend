package com.gogidix.courier.discountservice.shared.exception;

public class NotFoundException extends RuntimeException {

    private final String resourceType;
    private final String resourceId;

    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' not found", resourceType, resourceId));
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

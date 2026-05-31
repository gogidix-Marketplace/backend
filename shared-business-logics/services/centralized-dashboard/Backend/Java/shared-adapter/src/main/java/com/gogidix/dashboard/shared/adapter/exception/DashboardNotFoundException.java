package com.gogidix.dashboard.shared.adapter.exception;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * <p>This exception bridges between dashboard services and the Foundation
 * shared-exceptions library, providing consistent exception handling.
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
public class DashboardNotFoundException extends RuntimeException {

    private final String resourceType;
    private final String resourceId;

    public DashboardNotFoundException(String message) {
        super(message);
        this.resourceType = null;
        this.resourceId = null;
    }

    public DashboardNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public DashboardNotFoundException(String resourceType, String resourceId, String customMessage) {
        super(customMessage);
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

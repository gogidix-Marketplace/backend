package com.gogidix.shared.exceptions.domain.exception;

/**
 * Resource not found exception (404)
 */
public class ResourceNotFoundException extends BusinessException {
    
    private final String resourceType;
    private final String resourceId;
    
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId), 
              "RESOURCE_NOT_FOUND", 404);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        addContext("resourceType", resourceType);
        addContext("resourceId", resourceId);
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", 404);
        this.resourceType = "Unknown";
        this.resourceId = "Unknown";
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getResourceId() {
        return resourceId;
    }
}

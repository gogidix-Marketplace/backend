package com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception;

/**
 * Exception thrown when a conflict occurs, such as duplicate resource.
 */
public class ConflictException extends BaseDomainException {

    private final String resourceType;
    private final String conflictingValue;

    public ConflictException(String resourceType, String conflictingValue) {
        super(String.format("%s with value '%s' already exists", resourceType, conflictingValue));
        this.resourceType = resourceType;
        this.conflictingValue = conflictingValue;
    }

    public ConflictException(String message) {
        super(message);
        this.resourceType = "RESOURCE";
        this.conflictingValue = "UNKNOWN";
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
        this.resourceType = "RESOURCE";
        this.conflictingValue = "UNKNOWN";
    }

    public ConflictException(String resourceType, String conflictingValue, String errorCode) {
        super(String.format("%s with value '%s' already exists", resourceType, conflictingValue), errorCode);
        this.resourceType = resourceType;
        this.conflictingValue = conflictingValue;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getConflictingValue() {
        return conflictingValue;
    }

    @Override
    protected String deriveErrorCode() {
        return "RESOURCE_CONFLICT";
    }
}

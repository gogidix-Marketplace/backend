package com.gogidix.shared.exceptions;

import com.gogidix.shared.exceptions.domain.exception.BusinessException;

/**
 * Exception thrown when a conflict occurs during data operations.
 * <p>
 * This exception represents HTTP 409 Conflict scenarios where the request
 * conflicts with the current state of the target resource (e.g., duplicate
 * entries, concurrent modification conflicts, version mismatches).
 * </p>
 */
public class ConflictException extends BusinessException {

    private final String conflictType;
    private final String conflictingResourceId;

    /**
     * Creates a new ConflictException with a message.
     *
     * @param message the error message
     */
    public ConflictException(String message) {
        super(message, "CONFLICT", 409);
        this.conflictType = "Unknown";
        this.conflictingResourceId = "Unknown";
    }

    /**
     * Creates a new ConflictException with conflict type and resource ID.
     *
     * @param conflictType the type of conflict (e.g., "DUPLICATE", "VERSION_MISMATCH")
     * @param conflictingResourceId the ID of the conflicting resource
     */
    public ConflictException(String conflictType, String conflictingResourceId) {
        super(String.format("Conflict detected: %s with resource: %s", conflictType, conflictingResourceId),
              "CONFLICT", 409);
        this.conflictType = conflictType;
        this.conflictingResourceId = conflictingResourceId;
        addContext("conflictType", conflictType);
        addContext("conflictingResourceId", conflictingResourceId);
    }

    /**
     * Creates a new ConflictException with full details.
     *
     * @param message the error message
     * @param conflictType the type of conflict
     * @param conflictingResourceId the ID of the conflicting resource
     */
    public ConflictException(String message, String conflictType, String conflictingResourceId) {
        super(message, "CONFLICT", 409);
        this.conflictType = conflictType;
        this.conflictingResourceId = conflictingResourceId;
        addContext("conflictType", conflictType);
        addContext("conflictingResourceId", conflictingResourceId);
    }

    /**
     * Gets the type of conflict.
     *
     * @return the conflict type
     */
    public String getConflictType() {
        return conflictType;
    }

    /**
     * Gets the ID of the conflicting resource.
     *
     * @return the conflicting resource ID
     */
    public String getConflictingResourceId() {
        return conflictingResourceId;
    }

    /**
     * Static factory method for duplicate resource conflicts.
     *
     * @param resourceType the type of resource
     * @param resourceId the ID of the duplicate resource
     * @return a new ConflictException instance
     */
    public static ConflictException duplicate(String resourceType, String resourceId) {
        return new ConflictException("DUPLICATE", resourceId);
    }

    /**
     * Static factory method for version mismatch conflicts.
     *
     * @param resourceType the type of resource
     * @param resourceId the ID of the resource
     * @return a new ConflictException instance
     */
    public static ConflictException versionMismatch(String resourceType, String resourceId) {
        return new ConflictException(
            String.format("Version mismatch for %s with id: %s", resourceType, resourceId),
            "VERSION_MISMATCH",
            resourceId
        );
    }
}

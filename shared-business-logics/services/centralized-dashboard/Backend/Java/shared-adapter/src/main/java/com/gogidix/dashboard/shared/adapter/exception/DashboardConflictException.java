package com.gogidix.dashboard.shared.adapter.exception;

/**
 * Exception thrown when a conflict occurs (e.g., duplicate resource).
 * 
 * <p>This exception bridges between dashboard services and the Foundation
 * shared-exceptions library, providing consistent exception handling.
 * 
 * @author Gogidix Dashboard Team
 * @since 1.0.0
 */
public class DashboardConflictException extends RuntimeException {

    private final String resourceType;
    private final String conflictReason;

    public DashboardConflictException(String message) {
        super(message);
        this.resourceType = null;
        this.conflictReason = null;
    }

    public DashboardConflictException(String resourceType, String conflictReason) {
        super(String.format("Conflict with %s: %s", resourceType, conflictReason));
        this.resourceType = resourceType;
        this.conflictReason = conflictReason;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getConflictReason() {
        return conflictReason;
    }
}

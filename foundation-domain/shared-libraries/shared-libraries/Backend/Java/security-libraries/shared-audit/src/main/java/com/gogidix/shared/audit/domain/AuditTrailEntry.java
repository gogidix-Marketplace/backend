package com.gogidix.shared.audit.domain;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Value object representing a single entry in an audit trail.
 */
@Data
@Builder
@AllArgsConstructor
public class AuditTrailEntry {
    
    private final String eventId;
    private final LocalDateTime timestamp;
    private final String userId;
    private final String action;
    private final String resource;
    private final String result;
    private final String details;
    private final String correlationId;
    
    /**
     * Validates that this audit trail entry is complete
     */
    public boolean isComplete() {
        return eventId != null && !eventId.isEmpty() &&
               timestamp != null &&
               userId != null && !userId.isEmpty() &&
               action != null && !action.isEmpty() &&
               resource != null && !resource.isEmpty() &&
               result != null && !result.isEmpty();
    }
    
    /**
     * Gets a formatted string representation for logging
     */
    public String toLogFormat() {
        return String.format("[%s] User: %s, Action: %s, Resource: %s, Result: %s",
                timestamp.toString(), userId, action, resource, result);
    }
    
    /**
     * Gets a hash code for deduplication purposes
     */
    public String getDeduplicationHash() {
        return String.format("%s|%s|%s|%s", 
                userId, action, resource, timestamp.toString().substring(0, 16));
    }
}
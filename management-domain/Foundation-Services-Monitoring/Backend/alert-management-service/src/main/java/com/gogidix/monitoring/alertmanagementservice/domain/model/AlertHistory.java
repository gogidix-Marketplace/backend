package com.gogidix.monitoring.alertmanagementservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model for alert history.
 * Records all state changes for an alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertHistory {

    /**
     * Unique identifier.
     */
    private String id;

    /**
     * Associated alert ID.
     */
    private String alertId;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * State change type.
     */
    private StateChangeType stateChangeType;

    /**
     * Previous state.
     */
    private String previousState;

    /**
     * New state.
     */
    private String newState;

    /**
     * User who made the change.
     */
    private String changedBy;

    /**
     * Change comment.
     */
    private String comment;

    /**
     * Additional context.
     */
    private Map<String, Object> context;

    /**
     * Timestamp when the change occurred.
     */
    private Instant changedAt;

    /**
     * State change type enumeration.
     */
    public enum StateChangeType {
        CREATED,
        ACKNOWLEDGED,
        RESOLVED,
        CLOSED,
        REOPENED,
        COMMENT_ADDED,
        NOTIFICATION_SENT,
        NOTIFICATION_FAILED,
        ESCALATED
    }
}

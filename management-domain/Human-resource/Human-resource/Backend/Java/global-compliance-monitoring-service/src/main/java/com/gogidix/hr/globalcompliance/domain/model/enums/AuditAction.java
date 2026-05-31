package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Audit Action Enumeration
 * Defines the actions tracked in audit trail
 */
@Getter
public enum AuditAction {
    CREATED("Created"),
    UPDATED("Updated"),
    DELETED("Deleted"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    ESCALATED("Escalated"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    SUBMITTED("Submitted"),
    COMPLETED("Completed");

    private final String description;

    AuditAction(String description) {
        this.description = description;
    }
}

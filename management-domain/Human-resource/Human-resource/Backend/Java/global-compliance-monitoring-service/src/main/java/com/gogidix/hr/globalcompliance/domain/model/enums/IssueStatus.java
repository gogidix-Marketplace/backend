package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Issue Status Enumeration
 * Defines the status of non-compliance issues
 */
@Getter
public enum IssueStatus {
    OPEN("Open - New issue"),
    IN_PROGRESS("In Progress - Being addressed"),
    RESOLVED("Resolved - Issue fixed"),
    CLOSED("Closed - Verified and closed"),
    ESCALATED("Escalated - Raised to higher level");

    private final String description;

    IssueStatus(String description) {
        this.description = description;
    }
}

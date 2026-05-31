package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Issue Severity Enumeration
 * Defines the severity levels of non-compliance issues
 */
@Getter
public enum IssueSeverity {
    CRITICAL("Critical - Immediate action required"),
    HIGH("High - Urgent action required"),
    MEDIUM("Medium - Action required within defined timeline"),
    LOW("Low - Action required when possible");

    private final String description;

    IssueSeverity(String description) {
        this.description = description;
    }
}

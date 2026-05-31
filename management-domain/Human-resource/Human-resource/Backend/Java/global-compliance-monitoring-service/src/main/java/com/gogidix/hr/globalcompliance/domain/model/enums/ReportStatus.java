package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Report Status Enumeration
 * Defines the status of compliance reports
 */
@Getter
public enum ReportStatus {
    DRAFT("Draft - In preparation"),
    SUBMITTED("Submitted - Awaiting approval"),
    APPROVED("Approved - Accepted"),
    REJECTED("Rejected - Changes required"),
    PUBLISHED("Published - Final version");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }
}

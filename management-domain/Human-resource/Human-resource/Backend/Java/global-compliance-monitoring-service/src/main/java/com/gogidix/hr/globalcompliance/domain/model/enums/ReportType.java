package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Report Type Enumeration
 * Defines the types of compliance reports
 */
@Getter
public enum ReportType {
    MONTHLY("Monthly Report"),
    QUARTERLY("Quarterly Report"),
    ANNUAL("Annual Report"),
    AUDIT("Audit Report"),
    INCIDENT("Incident Report");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }
}

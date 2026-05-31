package com.gogidix.dashboard.reporting.domain.model;

/**
 * Report Type Enumeration
 */
public enum ReportType {
    EXECUTIVE_SUMMARY("Executive Summary Report"),
    PERFORMANCE_DASHBOARD("Performance Dashboard Report"),
    KPI_ANALYTICS("KPI Analytics Report"),
    OPERATIONAL_METRICS("Operational Metrics Report");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}

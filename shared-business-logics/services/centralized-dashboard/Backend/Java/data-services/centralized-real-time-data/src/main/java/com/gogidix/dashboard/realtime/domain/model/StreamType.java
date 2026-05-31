package com.gogidix.dashboard.realtime.domain.model;

/**
 * Stream Type Enumeration
 */
public enum StreamType {
    DASHBOARD_KPI("Dashboard KPI Stream"),
    PERFORMANCE_METRICS("Performance Metrics Stream"),
    REAL_TIME_EVENTS("Real-time Events Stream"),
    AGGREGATED_DATA("Aggregated Data Stream"),
    CRITICAL_ALERTS("Critical Alerts Stream"),
    REAL_TIME_DASHBOARD("Real-Time Dashboard Stream");

    private final String description;

    StreamType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }

    /**
     * Check if this stream type requires high resources
     */
    public boolean requiresHighResources() {
        return this == REAL_TIME_EVENTS || this == AGGREGATED_DATA;
    }
}

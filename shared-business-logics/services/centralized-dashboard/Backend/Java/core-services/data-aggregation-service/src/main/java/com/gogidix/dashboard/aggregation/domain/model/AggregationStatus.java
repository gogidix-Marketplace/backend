package com.gogidix.dashboard.aggregation.domain.model;

/**
 * Enumeration of aggregation request statuses.
 */
public enum AggregationStatus {
    PENDING("Pending", "Aggregation request is queued"),
    PROCESSING("Processing", "Aggregation is being processed"),
    COMPLETED("Completed", "Aggregation completed successfully"),
    FAILED("Failed", "Aggregation failed"),
    CANCELLED("Cancelled", "Aggregation was cancelled"),
    TIMEOUT("Timeout", "Aggregation timed out");

    private final String displayName;
    private final String description;

    AggregationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

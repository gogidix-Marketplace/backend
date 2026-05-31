package com.gogidix.hr.globalhrdashboard.domain.model;

/**
 * Enum representing different levels of data aggregation
 */
public enum AggregationLevel {
    GLOBAL("Global", "Aggregated across all regions and countries"),
    REGION("Regional", "Aggregated at region level"),
    COUNTRY("Country", "Aggregated at country level"),
    DEPARTMENT("Department", "Aggregated at department level"),
    TEAM("Team", "Aggregated at team level"),
    INDIVIDUAL("Individual", "Individual employee level");

    private final String displayName;
    private final String description;

    AggregationLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isGeographic() {
        return this == GLOBAL || this == REGION || this == COUNTRY;
    }

    public boolean isOrganizational() {
        return this == DEPARTMENT || this == TEAM;
    }

    public int getHierarchyLevel() {
        return switch (this) {
            case GLOBAL -> 1;
            case REGION -> 2;
            case COUNTRY -> 3;
            case DEPARTMENT -> 4;
            case TEAM -> 5;
            case INDIVIDUAL -> 6;
        };
    }

    public boolean isHigherThan(AggregationLevel other) {
        return this.getHierarchyLevel() < other.getHierarchyLevel();
    }
}

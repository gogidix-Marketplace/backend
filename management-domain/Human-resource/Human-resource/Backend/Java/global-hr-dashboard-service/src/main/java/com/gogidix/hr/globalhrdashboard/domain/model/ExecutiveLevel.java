package com.gogidix.hr.globalhrdashboard.domain.model;

/**
 * Enum representing executive levels for metric visibility and access
 */
public enum ExecutiveLevel {
    CHRO("Chief Human Resources Officer", "Chief Human Resources Officer - Full HR oversight"),
    CEO("Chief Executive Officer", "Chief Executive Officer - Overall organizational view"),
    CFO("Chief Financial Officer", "Chief Financial Officer - Financial HR metrics"),
    COO("Chief Operating Officer", "Chief Operating Officer - Operational HR metrics"),
    CTO("Chief Technology Officer", "Chief Technology Officer - Technical HR metrics"),
    CMO("Chief Marketing Officer", "Chief Marketing Officer - Marketing HR metrics"),
    CPO("Chief People Officer", "Chief People Officer - People and culture metrics"),
    ALL("All Executives", "Metrics visible to all executive levels");

    private final String title;
    private final String description;

    ExecutiveLevel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCLevel() {
        return this != ALL;
    }

    public boolean hasFullAccess() {
        return this == CHRO || this == CEO || this == ALL;
    }

    public boolean hasFinancialAccess() {
        return this == CHRO || this == CEO || this == CFO || this == ALL;
    }

    public boolean hasOperationalAccess() {
        return this == CHRO || this == CEO || this == COO || this == ALL;
    }
}

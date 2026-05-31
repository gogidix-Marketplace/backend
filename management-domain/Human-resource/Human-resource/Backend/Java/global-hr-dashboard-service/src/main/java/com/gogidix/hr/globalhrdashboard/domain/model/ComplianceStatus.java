package com.gogidix.hr.globalhrdashboard.domain.model;

/**
 * Enum representing the compliance status of HR requirements
 */
public enum ComplianceStatus {
    COMPLIANT("Compliant", "All requirements are met", 100),
    AT_RISK("At Risk", "Some requirements need attention", 70),
    NON_COMPLIANT("Non-Compliant", "Critical requirements are not met", 0),
    PENDING_REVIEW("Pending Review", "Compliance status under review", 50),
    EXEMPT("Exempt", "Requirements exempt for this entity", 0);

    private final String displayName;
    private final String description;
    private final int minScore;

    ComplianceStatus(String displayName, String description, int minScore) {
        this.displayName = displayName;
        this.description = description;
        this.minScore = minScore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMinScore() {
        return minScore;
    }

    public boolean requiresAction() {
        return this == AT_RISK || this == NON_COMPLIANT;
    }

    public boolean isAcceptable() {
        return this == COMPLIANT || this == EXEMPT;
    }

    public static ComplianceStatus fromScore(double score) {
        if (score >= 95) {
            return COMPLIANT;
        } else if (score >= 70) {
            return AT_RISK;
        } else {
            return NON_COMPLIANT;
        }
    }
}

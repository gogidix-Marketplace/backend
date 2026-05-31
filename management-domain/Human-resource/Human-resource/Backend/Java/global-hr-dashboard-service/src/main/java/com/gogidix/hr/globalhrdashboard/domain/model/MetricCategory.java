package com.gogidix.hr.globalhrdashboard.domain.model;

/**
 * Enum representing different categories of HR metrics
 */
public enum MetricCategory {
    HEADCOUNT("Headcount Metrics", "Total number of employees across the organization"),
    RETENTION("Retention Metrics", "Employee retention and turnover statistics"),
    COMPLIANCE("Compliance Metrics", "Legal and regulatory compliance measurements"),
    PERFORMANCE("Performance Metrics", "Employee performance and productivity data"),
    DIVERSITY("Diversity Metrics", "Workforce diversity and inclusion measurements"),
    ENGAGEMENT("Engagement Metrics", "Employee engagement and satisfaction levels"),
    ABSENTEEISM("Absenteeism Metrics", "Employee attendance and absence tracking"),
    TRAINING("Training Metrics", "Learning and development statistics");

    private final String displayName;
    private final String description;

    MetricCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isStrategic() {
        return this == HEADCOUNT || this == RETENTION || this == PERFORMANCE;
    }

    public boolean isOperational() {
        return this == COMPLIANCE || this == ABSENTEEISM;
    }

    public boolean isCultural() {
        return this == DIVERSITY || this == ENGAGEMENT || this == TRAINING;
    }
}

package com.gogidix.centralconfiguration.environmentservice.domain.model;

/**
 * Environment Type enumeration.
 */
public enum EnvironmentType {
    DEVELOPMENT("development", "Development environment"),
    STAGING("staging", "Staging environment"),
    QA("qa", "Quality Assurance environment"),
    UAT("uat", "User Acceptance Testing environment"),
    PRODUCTION("production", "Production environment"),
    DR("dr", "Disaster Recovery environment");

    private final String code;
    private final String description;

    EnvironmentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

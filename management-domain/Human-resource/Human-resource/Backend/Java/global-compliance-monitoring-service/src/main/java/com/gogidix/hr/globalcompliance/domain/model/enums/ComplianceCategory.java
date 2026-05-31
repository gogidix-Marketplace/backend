package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Compliance Category Enumeration
 * Defines the categories of compliance requirements
 */
@Getter
public enum ComplianceCategory {
    LABOR_LAW("Labor Law"),
    DATA_PRIVACY("Data Privacy"),
    HEALTH_SAFETY("Health and Safety"),
    IMMIGRATION("Immigration"),
    EQUALITY("Equality and Diversity"),
    ANTI_CORRUPTION("Anti-Corruption"),
    FINANCIAL("Financial Compliance"),
    ENVIRONMENTAL("Environmental");

    private final String description;

    ComplianceCategory(String description) {
        this.description = description;
    }
}

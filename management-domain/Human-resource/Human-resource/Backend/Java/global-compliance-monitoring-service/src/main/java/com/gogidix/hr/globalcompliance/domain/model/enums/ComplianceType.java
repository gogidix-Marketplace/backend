package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Compliance Type Enumeration
 * Defines the types of compliance requirements
 */
@Getter
public enum ComplianceType {
    MANDATORY("Mandatory - Required by law"),
    VOLUNTARY("Voluntary - Best practice"),
    CONTRACTUAL("Contractual - Required by contract"),
    REGULATORY("Regulatory - Required by regulator");

    private final String description;

    ComplianceType(String description) {
        this.description = description;
    }
}

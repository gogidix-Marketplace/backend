package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Check Result Enumeration
 * Defines the result of compliance checks
 */
@Getter
public enum CheckResult {
    COMPLIANT("Compliant - Meets requirements"),
    NON_COMPLIANT("Non-Compliant - Does not meet requirements"),
    PARTIALLY_COMPLIANT("Partially Compliant - Some issues found"),
    NOT_TESTED("Not Tested - Not yet evaluated");

    private final String description;

    CheckResult(String description) {
        this.description = description;
    }
}

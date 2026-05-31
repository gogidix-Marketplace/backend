package com.gogidix.hr.globalcompliance.domain.model.enums;

import lombok.Getter;

/**
 * Check Status Enumeration
 * Defines the status of compliance checks
 */
@Getter
public enum CheckStatus {
    PENDING("Pending - Not started"),
    IN_PROGRESS("In Progress - Being checked"),
    PASSED("Passed - Compliance verified"),
    FAILED("Failed - Non-compliance found"),
    WAIVED("Waived - Exception granted"),
    NOT_APPLICABLE("Not Applicable - Does not apply");

    private final String description;

    CheckStatus(String description) {
        this.description = description;
    }
}

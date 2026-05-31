package com.gogidix.hr.payroll.domain.enums;

/**
 * Enumeration for Payroll Status
 */
public enum PayrollStatus {
    DRAFT("Draft"),
    PENDING_APPROVAL("Pending Approval"),
    APPROVED("Approved"),
    PROCESSED("Processed"),
    PAID("Paid"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    PayrollStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

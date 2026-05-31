package com.gogidix.hr.payroll.domain.enums;

/**
 * Enumeration for Payment Method
 */
public enum PaymentMethod {
    DIRECT_DEPOSIT("Direct Deposit"),
    PAPER_CHECK("Paper Check"),
    WIRE_TRANSFER("Wire Transfer"),
    PREPAID_CARD("Prepaid Card"),
    CASH("Cash"),
    ELECTRONIC_TRANSFER("Electronic Transfer");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

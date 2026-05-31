package com.gogidix.hr.payroll.domain.enums;

/**
 * Enumeration for Tax Type
 */
public enum TaxType {
    FEDERAL_INCOME_TAX("Federal Income Tax"),
    STATE_INCOME_TAX("State Income Tax"),
    LOCAL_INCOME_TAX("Local Income Tax"),
    SOCIAL_SECURITY("Social Security"),
    MEDICARE("Medicare"),
    FEDERAL_UNEMPLOYMENT_TAX("Federal Unemployment Tax (FUTA)"),
    STATE_UNEMPLOYMENT_TAX("State Unemployment Tax (SUTA)"),
    DISABILITY_INSURANCE("Disability Insurance"),
    WORKERS_COMPENSATION("Workers Compensation"),
    OTHER_TAX("Other Tax");

    private final String displayName;

    TaxType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

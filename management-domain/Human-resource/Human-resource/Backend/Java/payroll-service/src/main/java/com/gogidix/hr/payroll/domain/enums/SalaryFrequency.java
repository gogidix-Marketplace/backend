package com.gogidix.hr.payroll.domain.enums;

/**
 * Enumeration for Salary Frequency
 */
public enum SalaryFrequency {
    WEEKLY("Weekly", 52),
    BI_WEEKLY("Bi-Weekly", 26),
    SEMI_MONTHLY("Semi-Monthly", 24),
    MONTHLY("Monthly", 12),
    BI_MONTHLY("Bi-Monthly", 6),
    QUARTERLY("Quarterly", 4),
    ANNUALLY("Annually", 1);

    private final String displayName;
    private final int periodsPerYear;

    SalaryFrequency(String displayName, int periodsPerYear) {
        this.displayName = displayName;
        this.periodsPerYear = periodsPerYear;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPeriodsPerYear() {
        return periodsPerYear;
    }
}

package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Salary Component
 * Represents a component of salary structure (e.g., basic pay, allowances, bonuses)
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SalaryComponent extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String componentId;
    private String componentCode;
    private String componentName;
    private ComponentType componentType;
    private CalculationType calculationType;
    private String description;
    private BigDecimal amount;
    private BigDecimal percentage;
    private String percentageOf;
    private Boolean isTaxable;
    private Boolean isSubjectToPF;
    private Boolean isSubjectToESI;
    private Boolean isSubjectToPT;
    private Boolean isActive;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String applicability;
    private String category;
    private Integer priority;
    private String formula;
    private String glAccount;
    private String costCenter;
    private Boolean requiresApproval;
    private Integer minAmount;
    private Integer maxAmount;
    private String currency;
    private YearMonth applicableFrom;
    private YearMonth applicableTo;
    private String frequency;
    private Boolean isRecurring;
    private String basedOn;
    private String notes;

    /**
     * Calculate component amount based on basic salary
     */
    public BigDecimal calculateAmount(BigDecimal basicSalary) {
        if (amount != null && calculationType == CalculationType.FLAT) {
            return amount;
        }

        if (percentage != null && calculationType == CalculationType.PERCENTAGE && basicSalary != null) {
            return basicSalary.multiply(percentage).divide(BigDecimal.valueOf(100));
        }

        if (formula != null) {
            // Formula calculation would be implemented here with a formula engine
            return amount != null ? amount : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    /**
     * Check if component is effective for given date
     */
    public boolean isEffectiveForDate(LocalDate date) {
        if (!isActive) {
            return false;
        }

        boolean afterStart = effectiveFrom == null || !date.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !date.isAfter(effectiveTo);

        return afterStart && beforeEnd;
    }

    /**
     * Check if component is applicable for given period
     */
    public boolean isApplicableForPeriod(YearMonth period) {
        if (!isActive) {
            return false;
        }

        if (applicableFrom != null && period.isBefore(applicableFrom)) {
            return false;
        }

        if (applicableTo != null && period.isAfter(applicableTo)) {
            return false;
        }

        return true;
    }

    /**
     * Check if amount is within limits
     */
    public boolean isAmountWithinLimits(BigDecimal checkAmount) {
        if (minAmount != null && checkAmount.compareTo(BigDecimal.valueOf(minAmount)) < 0) {
            return false;
        }

        if (maxAmount != null && checkAmount.compareTo(BigDecimal.valueOf(maxAmount)) > 0) {
            return false;
        }

        return true;
    }

    public enum ComponentType {
        EARNING,
        DEDUCTION,
        TAX,
        BENEFIT,
        ALLOWANCE,
        BONUS,
        COMMISSION,
        OVERTIME,
        REIMBURSEMENT
    }

    public enum CalculationType {
        FLAT,
        PERCENTAGE,
        FORMULA,
        TIERED
    }
}

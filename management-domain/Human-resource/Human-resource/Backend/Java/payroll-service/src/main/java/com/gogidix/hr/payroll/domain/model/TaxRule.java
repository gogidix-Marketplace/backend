package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.enums.TaxType;
import com.gogidix.hr.payroll.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Tax Rule
 * Represents tax calculation rules for different jurisdictions
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaxRule extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String stateCode;
    private String taxName;
    private String taxCode;
    private TaxType taxType;
    private String description;
    private BigDecimal rate;
    private BigDecimal flatAmount;
    private BigDecimal thresholdMin;
    private BigDecimal thresholdMax;
    private BigDecimal exemptionAmount;
    private Boolean isPercentage;
    private Boolean isActive;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String calculationMethod;
    private List<TaxBracket> taxBrackets = new ArrayList<>();
    private String jurisdictionType;
    private String jurisdictionLevel;
    private Integer priority;
    private Boolean isCumulative;
    private Boolean employerMatch;
    private BigDecimal employerMatchRate;
    private String employerMatchCode;
    private YearMonth applicableFrom;
    private YearMonth applicableTo;
    private Integer filingStatus;
    private Integer minExemptions;
    private Integer maxExemptions;
    private BigDecimal additionalWithholding;
    private String formula;
    private String referenceCode;
    private String notes;

    /**
     * Calculate tax amount based on taxable income
     */
    public BigDecimal calculateTax(BigDecimal taxableIncome, Integer exemptions, BigDecimal yearToDate) {
        if (!isActive || isExpired()) {
            return BigDecimal.ZERO;
        }

        if (!isInRange(taxableIncome)) {
            return BigDecimal.ZERO;
        }

        BigDecimal tax = BigDecimal.ZERO;

        if (taxBrackets != null && !taxBrackets.isEmpty()) {
            tax = calculateWithBrackets(taxableIncome);
        } else if (Boolean.TRUE.equals(isPercentage)) {
            tax = taxableIncome.multiply(rate).divide(BigDecimal.valueOf(100));
            if (exemptions != null && exemptionAmount != null) {
                tax = tax.subtract(exemptionAmount.multiply(BigDecimal.valueOf(exemptions)));
            }
        } else if (flatAmount != null) {
            tax = flatAmount;
        }

        return tax.max(BigDecimal.ZERO);
    }

    /**
     * Calculate tax using brackets
     */
    private BigDecimal calculateWithBrackets(BigDecimal taxableIncome) {
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal remainingIncome = taxableIncome;

        for (TaxBracket bracket : taxBrackets) {
            if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            if (taxableIncome.compareTo(bracket.getMin()) >= 0) {
                BigDecimal taxableInBracket = remainingIncome.min(
                        bracket.getMax().subtract(bracket.getMin())
                );
                BigDecimal bracketTax = taxableInBracket.multiply(bracket.getRate())
                        .divide(BigDecimal.valueOf(100));
                totalTax = totalTax.add(bracketTax);
                remainingIncome = remainingIncome.subtract(taxableInBracket);
            }
        }

        return totalTax;
    }

    /**
     * Check if tax rule is expired
     */
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }

    /**
     * Check if tax rule is effective
     */
    public boolean isEffective() {
        return effectiveDate != null && !LocalDate.now().isBefore(effectiveDate);
    }

    /**
     * Check if amount is in range
     */
    public boolean isInRange(BigDecimal amount) {
        boolean aboveMin = thresholdMin == null || amount.compareTo(thresholdMin) >= 0;
        boolean belowMax = thresholdMax == null || amount.compareTo(thresholdMax) <= 0;
        return aboveMin && belowMax;
    }

    /**
     * Get employer match amount
     */
    public BigDecimal getEmployerMatchAmount(BigDecimal employeeContribution) {
        if (!Boolean.TRUE.equals(employerMatch) || employerMatchRate == null) {
            return BigDecimal.ZERO;
        }
        return employeeContribution.multiply(employerMatchRate).divide(BigDecimal.valueOf(100));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxBracket {
        private BigDecimal min;
        private BigDecimal max;
        private BigDecimal rate;
        private Integer priority;
    }
}

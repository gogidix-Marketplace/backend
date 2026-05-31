package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model for Tax Configuration
 * Represents tax settings for a country
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaxConfiguration extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String taxCode;
    private String taxName;
    private String taxType;
    private String description;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String taxAuthority;
    private String taxIdPattern;
    private String registrationNumberPattern;
    private BigDecimal taxRate;
    private String taxRateType;
    private BigDecimal taxFreeThreshold;
    private List<TaxBracket> taxBrackets;
    private List<Deduction> allowableDeductions;
    private List<Exemption> exemptions;
    private List<Credit> taxCredits;
    private String filingFrequency;
    private Integer filingFrequencyDays;
    private String filingDeadline;
    private String paymentFrequency;
    private Integer paymentDaysAfterFiling;
    private String lateFilingPenaltyRate;
    private String latePaymentPenaltyRate;
    private String interestRateOnLatePayment;
    private Boolean withHoldingRequired;
    private BigDecimal withholdingRate;
    private String estimatedTaxPaymentRequired;
    private String estimatedTaxFrequency;
    private Boolean electronicFilingRequired;
    private String electronicFilingPlatform;
    private Boolean advanceTaxPaymentRequired;
    private String advanceTaxPaymentMethod;
    private String taxResidencyRules;
    private String doubleTaxationTreatyCountries;
    private String foreignTaxCreditRules;
    private String socialSecurityTaxRules;
    private String payrollTaxRules;
    private String vatRules;
    private String customsDutyRules;
    private String exciseTaxRules;
    private String environmentalTaxes;
    private String otherLocalTaxes;
    private Map<String, String> customFields;
    private String complianceChecklist;
    private String requiredDocumentation;
    private String auditRequirements;
    private String recordRetentionPeriod;
    private String penaltyCalculationMethod;
    private String appealProcess;
    private String disputeResolution;
    private Boolean isActive;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxBracket {
        private String bracketCode;
        private String bracketName;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private BigDecimal taxRate;
        private BigDecimal fixedAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Deduction {
        private String deductionCode;
        private String deductionName;
        private BigDecimal maxAmount;
        private String calculationMethod;
        private Boolean requiresDocumentation;
        private String applicableConditions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Exemption {
        private String exemptionCode;
        private String exemptionName;
        private BigDecimal maxAmount;
        private String eligibilityCriteria;
        private Boolean requiresDocumentation;
        private String applicationProcess;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credit {
        private String creditCode;
        private String creditName;
        private BigDecimal amount;
        private String eligibilityCriteria;
        private Boolean isRefundable;
        private String carryForwardRules;
    }

    public BigDecimal calculateTax(BigDecimal taxableIncome) {
        if (taxBrackets == null || taxableIncome == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalTax = BigDecimal.ZERO;
        for (TaxBracket bracket : taxBrackets) {
            if (taxableIncome.compareTo(bracket.getMinAmount()) >= 0) {
                BigDecimal taxableInBracket = taxableIncome.subtract(bracket.getMinAmount());
                if (bracket.getMaxAmount() != null) {
                    taxableInBracket = taxableInBracket.min(
                            bracket.getMaxAmount().subtract(bracket.getMinAmount())
                    );
                }
                totalTax = totalTax.add(bracket.getTaxRate().multiply(taxableInBracket));
                if (bracket.getMaxAmount() == null || taxableIncome.compareTo(bracket.getMaxAmount()) <= 0) {
                    break;
                }
            }
        }
        return totalTax;
    }

    public boolean isEffectiveOn(LocalDate date) {
        return date != null &&
                (effectiveDate == null || !date.isBefore(effectiveDate)) &&
                (expiryDate == null || !date.isAfter(expiryDate));
    }

    public BigDecimal calculateWithholding(BigDecimal grossPay) {
        if (withholdingRate == null || grossPay == null) {
            return BigDecimal.ZERO;
        }
        return grossPay.multiply(withholdingRate);
    }
}

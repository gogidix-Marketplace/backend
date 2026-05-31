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
 * Domain model for Payroll Configuration
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayrollConfig extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String configCode;
    private String configName;
    private String description;
    private String currency;
    private String currencySymbol;
    private Integer currencyDecimalPlaces;
    private String payrollFrequency;
    private String payrollPeriodType;
    private Integer payPeriodDays;
    private LocalDate firstPayPeriodDate;
    private String payDayOfMonth;
    private String payDayOfWeek;
    private String payProcessingCutoffDay;
    private Integer advanceNoticeDays;
    private String roundingMethod;
    private Integer roundingPrecision;
    private String taxCalculationMethod;
    private String socialSecurityRate;
    private String employerSocialSecurityRate;
    private String employeeSocialSecurityRate;
    private String healthInsuranceRate;
    private String employerHealthInsuranceRate;
    private String employeeHealthInsuranceRate;
    private String unemploymentInsuranceRate;
    private String employerUnemploymentInsuranceRate;
    private String employeeUnemploymentInsuranceRate;
    private String pensionContributionRate;
    private String employerPensionRate;
    private String employeePensionRate;
    private BigDecimal taxFreeAllowance;
    private List<TaxBracket> taxBrackets;
    private List<Allowance> allowances;
    private List<Deduction> deductions;
    private String overtimeCalculationMethod;
    private Double overtimeMultiplier;
    private Double nightShiftMultiplier;
    private Double weekendMultiplier;
    private Double holidayMultiplier;
    private String payslipFormat;
    private Boolean electronicPayslipRequired;
    private String payslipDeliveryMethod;
    private Integer payslipAccessPeriodDays;
    private String payrollReportFormat;
    private List<String> requiredReportFields;
    private Map<String, String> customFields;
    private String paymentMethod;
    private List<String> supportedPaymentMethods;
    private String bankAccountVerificationRequired;
    private Boolean withholdIncomeTax;
    private Boolean withholdSocialSecurity;
    private Boolean withholdHealthInsurance;
    private Boolean withholdPension;
    private Boolean withholdUnionDues;
    private String garnishmentRules;
    private String backPayCalculationMethod;
    private String finalPayCalculationMethod;
    private String severanceCalculationMethod;
    private String payDataTransferProtocol;
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
    public static class Allowance {
        private String allowanceCode;
        private String allowanceName;
        private BigDecimal amount;
        private String calculationMethod;
        private Boolean isTaxable;
        private Boolean requiresProof;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Deduction {
        private String deductionCode;
        private String deductionName;
        private BigDecimal amount;
        private String calculationMethod;
        private Boolean isMandatory;
        private String priority;
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

    public BigDecimal calculateNetPay(BigDecimal grossPay, List<Deduction> applicableDeductions) {
        BigDecimal netPay = grossPay != null ? grossPay : BigDecimal.ZERO;

        if (applicableDeductions != null) {
            for (Deduction deduction : applicableDeductions) {
                if (deduction.getAmount() != null) {
                    netPay = netPay.subtract(deduction.getAmount());
                }
            }
        }

        return netPay.max(BigDecimal.ZERO);
    }
}

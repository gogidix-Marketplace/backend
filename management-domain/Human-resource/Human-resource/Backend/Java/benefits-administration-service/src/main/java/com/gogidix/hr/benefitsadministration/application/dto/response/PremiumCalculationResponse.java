package com.gogidix.hr.benefitsadministration.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for premium calculation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumCalculationResponse {

    private String planId;
    private String planName;
    private String employeeId;
    private String coverageLevel;
    private String coverageOptionCode;
    private Integer numberOfDependents;
    private BigDecimal employeePremium;
    private BigDecimal employerPremium;
    private BigDecimal totalPremium;
    private String currency;
    private String deductionFrequency;
    private LocalDate effectiveDate;
    private List<PremiumBreakdown> breakdown;
    private CalculationMetadata metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PremiumBreakdown {
        private String component;
        private String description;
        private BigDecimal amount;
        private Boolean isEmployeePaid;
        private LocalDate effectiveDate;
        private LocalDate endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalculationMetadata {
        private String calculationMethod;
        private LocalDate calculationDate;
        private Boolean isEstimate;
        private List<String> factors;
        private String notes;
    }
}

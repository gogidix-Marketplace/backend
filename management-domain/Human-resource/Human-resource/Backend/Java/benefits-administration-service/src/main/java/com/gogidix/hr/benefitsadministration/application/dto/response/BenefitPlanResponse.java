package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.enums.DeductionFrequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for benefit plan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitPlanResponse {

    private String id;
    private String tenantId;
    private String countryCode;
    private String planId;
    private String planCode;
    private String planName;
    private String description;
    private BenefitType benefitType;
    private BenefitStatus status;
    private String providerId;
    private String providerName;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
    private BigDecimal totalCost;
    private String currency;
    private DeductionFrequency deductionFrequency;
    private List<CoverageOptionResponse> coverageOptions;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private Integer enrollmentWindowDays;
    private Boolean requiresEvidence;
    private List<String> requiredDocuments;
    private Integer minEmployees;
    private Integer maxEmployees;
    private Boolean isVoluntary;
    private Boolean isTaxable;
    private String taxCode;
    private Boolean hasWaitingPeriod;
    private Integer waitingPeriodDays;
    private Boolean eligibilityCheckRequired;
    private String eligibilityCriteria;
    private String termsAndConditions;
    private String summary;
    private String category;
    private Integer priority;
    private String contactInfo;
    private String websiteUrl;
    private String brochureUrl;
    private List<String> coveredServices;
    private List<String> excludedServices;
    private BigDecimal annualLimit;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoverageOptionResponse {
        private String optionCode;
        private String optionName;
        private String description;
        private String level;
        private BigDecimal employeeCost;
        private BigDecimal employerCost;
        private Boolean isAvailable;
    }
}

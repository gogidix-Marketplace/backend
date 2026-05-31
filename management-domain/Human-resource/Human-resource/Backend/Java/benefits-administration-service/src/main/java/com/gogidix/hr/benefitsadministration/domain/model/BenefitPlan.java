package com.gogidix.hr.benefitsadministration.domain.model;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.enums.DeductionFrequency;
import com.gogidix.hr.benefitsadministration.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitPlan extends BaseEntity {

    private String tenantId;
    private String countryCode;
    private String planId;
    private String planCode;
    private String planName;
    private String description;
    private BenefitType benefitType;
    private BenefitStatus status = BenefitStatus.ACTIVE;
    private String providerId;
    private String providerName;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
    private BigDecimal totalCost;
    private String currency;
    private DeductionFrequency deductionFrequency;
    private List<CoverageOption> coverageOptions = new ArrayList<>();
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
    private List<String> coveredServices = new ArrayList<>();
    private List<String> excludedServices = new ArrayList<>();
    private BigDecimal annualLimit;
    private String notes;
    private Boolean isActive;

    public boolean isEffective() {
        LocalDate now = LocalDate.now();
        boolean afterStart = effectiveDate == null || !now.isBefore(effectiveDate);
        boolean beforeEnd = expiryDate == null || !now.isAfter(expiryDate);
        return afterStart && beforeEnd && BenefitStatus.ACTIVE.equals(status);
    }

    public boolean isWithinEnrollmentWindow() {
        if (enrollmentWindowDays == null) return false;
        return LocalDate.now().minusDays(enrollmentWindowDays).isBefore(effectiveDate);
    }

    private LocalDate now() {
        return LocalDate.now();
    }

    public BigDecimal getEmployeeContributionForCoverage(String coverageLevel) {
        return employeeContribution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoverageOption {
        private String optionCode;
        private String optionName;
        private String description;
        private CoverageLevelType level;
        private BigDecimal employeeCost;
        private BigDecimal employerCost;
        private Boolean isAvailable;
    }

    public enum CoverageLevelType {
        EMPLOYEE_ONLY,
        EMPLOYEE_PLUS_SPOUSE,
        EMPLOYEE_PLUS_CHILDREN,
        FAMILY,
        EMPLOYEE_PLUS_ONE
    }
}

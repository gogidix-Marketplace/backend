package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.enums.DeductionFrequency;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitPlan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating a benefit plan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBenefitPlanRequest {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 3, message = "Country code must be 2-3 characters")
    private String countryCode;

    @NotBlank(message = "Plan code is required")
    @Size(max = 50, message = "Plan code must not exceed 50 characters")
    private String planCode;

    @NotBlank(message = "Plan name is required")
    @Size(max = 200, message = "Plan name must not exceed 200 characters")
    private String planName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Benefit type is required")
    private BenefitType benefitType;

    @Builder.Default
    private BenefitStatus status = BenefitStatus.ACTIVE;

    @NotBlank(message = "Provider ID is required")
    private String providerId;

    @Size(max = 200, message = "Provider name must not exceed 200 characters")
    private String providerName;

    @NotNull(message = "Employee contribution is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Employee contribution must be non-negative")
    private BigDecimal employeeContribution;

    @NotNull(message = "Employer contribution is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Employer contribution must be non-negative")
    private BigDecimal employerContribution;

    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total cost must be non-negative")
    private BigDecimal totalCost;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters (ISO 4217)")
    private String currency;

    @NotNull(message = "Deduction frequency is required")
    private DeductionFrequency deductionFrequency;

    private List<CoverageOptionRequest> coverageOptions;

    @NotNull(message = "Effective date is required")
    @FutureOrPresent(message = "Effective date must be today or in the future")
    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    @Min(value = 0, message = "Enrollment window days must be non-negative")
    private Integer enrollmentWindowDays;

    private Boolean requiresEvidence;

    private List<String> requiredDocuments;

    @Min(value = 0, message = "Minimum employees must be non-negative")
    private Integer minEmployees;

    @Min(value = 0, message = "Maximum employees must be non-negative")
    private Integer maxEmployees;

    private Boolean isVoluntary;

    private Boolean isTaxable;

    @Size(max = 50, message = "Tax code must not exceed 50 characters")
    private String taxCode;

    private Boolean hasWaitingPeriod;

    @Min(value = 0, message = "Waiting period days must be non-negative")
    private Integer waitingPeriodDays;

    private Boolean eligibilityCheckRequired;

    @Size(max = 1000, message = "Eligibility criteria must not exceed 1000 characters")
    private String eligibilityCriteria;

    @Size(max = 5000, message = "Terms and conditions must not exceed 5000 characters")
    private String termsAndConditions;

    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Min(value = 0, message = "Priority must be non-negative")
    private Integer priority;

    @Size(max = 500, message = "Contact info must not exceed 500 characters")
    private String contactInfo;

    @Size(max = 500, message = "Website URL must not exceed 500 characters")
    private String websiteUrl;

    @Size(max = 500, message = "Brochure URL must not exceed 500 characters")
    private String brochureUrl;

    private List<String> coveredServices;

    private List<String> excludedServices;

    private BigDecimal annualLimit;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private Boolean isActive;

    @AssertTrue(message = "Expiry date must be after effective date")
    private boolean isDateRangeValid() {
        if (effectiveDate == null || expiryDate == null) {
            return true;
        }
        return expiryDate.isAfter(effectiveDate);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoverageOptionRequest {
        @NotBlank(message = "Option code is required")
        private String optionCode;

        @NotBlank(message = "Option name is required")
        private String optionName;

        private String description;

        private BenefitPlan.CoverageLevelType level;

        @NotNull(message = "Employee cost is required")
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal employeeCost;

        @NotNull(message = "Employer cost is required")
        @DecimalMin(value = "0.0", inclusive = true)
        private BigDecimal employerCost;

        @Builder.Default
        private Boolean isAvailable = true;
    }
}

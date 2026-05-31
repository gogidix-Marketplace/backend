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
 * Request DTO for updating a benefit plan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBenefitPlanRequest {

    @Size(max = 200, message = "Plan name must not exceed 200 characters")
    private String planName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private BenefitType benefitType;

    private BenefitStatus status;

    private String providerId;

    @Size(max = 200, message = "Provider name must not exceed 200 characters")
    private String providerName;

    @DecimalMin(value = "0.0", inclusive = true, message = "Employee contribution must be non-negative")
    private BigDecimal employeeContribution;

    @DecimalMin(value = "0.0", inclusive = true, message = "Employer contribution must be non-negative")
    private BigDecimal employerContribution;

    @DecimalMin(value = "0.0", inclusive = true, message = "Total cost must be non-negative")
    private BigDecimal totalCost;

    private DeductionFrequency deductionFrequency;

    private List<CreateBenefitPlanRequest.CoverageOptionRequest> coverageOptions;

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

    @DecimalMin(value = "0.0", inclusive = true, message = "Annual limit must be non-negative")
    private BigDecimal annualLimit;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private Boolean isActive;
}

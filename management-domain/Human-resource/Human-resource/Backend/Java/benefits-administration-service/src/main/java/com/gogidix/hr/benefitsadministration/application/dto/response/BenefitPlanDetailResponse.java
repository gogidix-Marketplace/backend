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
 * Response DTO for detailed benefit plan information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitPlanDetailResponse extends BenefitPlanResponse {

    private ProviderDetail provider;
    private EnrollmentDetails enrollmentDetails;
    private CostBreakdown costBreakdown;
    private List<EligibilityRequirement> eligibilityRequirements;
    private List<DocumentRequirement> documentRequirements;
    private PlanStatistics statistics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderDetail {
        private String providerId;
        private String providerName;
        private String contactEmail;
        private String contactPhone;
        private String website;
        private String address;
        private String claimsContactEmail;
        private String claimsContactPhone;
        private String claimsPortalUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentDetails {
        private Boolean isOpenForEnrollment;
        private LocalDate enrollmentStartDate;
        private LocalDate enrollmentEndDate;
        private Integer enrollmentWindowDays;
        private Boolean hasWaitingPeriod;
        private Integer waitingPeriodDays;
        private Boolean requiresEvidence;
        private List<String> requiredDocuments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostBreakdown {
        private BigDecimal baseEmployeeCost;
        private BigDecimal baseEmployerCost;
        private BigDecimal totalCost;
        private String currency;
        private DeductionFrequency deductionFrequency;
        private List<CostByCoverage> costByCoverageLevel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostByCoverage {
        private String coverageLevel;
        private BigDecimal employeeCost;
        private BigDecimal employerCost;
        private BigDecimal totalCost;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EligibilityRequirement {
        private String requirement;
        private String description;
        private Boolean isRequired;
        private String validationRule;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentRequirement {
        private String documentType;
        private String description;
        private Boolean isRequired;
        private String uploadDeadline;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanStatistics {
        private Long totalEnrollments;
        private Long activeEnrollments;
        private Long pendingEnrollments;
        private Double averagePremium;
        private Integer minEnrollmentCount;
        private Integer maxEnrollmentCount;
    }
}

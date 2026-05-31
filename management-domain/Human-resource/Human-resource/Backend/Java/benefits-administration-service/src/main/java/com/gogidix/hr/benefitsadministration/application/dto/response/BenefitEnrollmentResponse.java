package com.gogidix.hr.benefitsadministration.application.dto.response;

import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitEnrollmentResponse {

    private String id;
    private String tenantId;
    private String employeeId;
    private String planId;
    private String planName;
    private String coverageLevel;
    private String coverageOptionCode;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private EnrollmentStatus status;
    private List<DependentCoverageResponse> dependents;
    private BigDecimal employeePremium;
    private BigDecimal employerPremium;
    private BigDecimal totalPremium;
    private String currency;
    private String notes;
    private Boolean waiveCoverage;
    private String waiverReason;
    private List<String> evidenceDocuments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependentCoverageResponse {
        private String id;
        private String firstName;
        private String lastName;
        private String relationship;
        private LocalDate dateOfBirth;
        private String gender;
        private String address;
        private Boolean isStudent;
        private Boolean isDisabled;
        private String ssnLast4;
        private LocalDate effectiveDate;
        private LocalDate endDate;
        private String notes;
    }
}

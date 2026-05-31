package com.gogidix.hr.benefitsadministration.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for employee benefit summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBenefitSummaryResponse {

    private String employeeId;
    private String tenantId;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String jobTitle;
    private LocalDate hireDate;
    private Boolean isEligibleForBenefits;
    private List<EnrollmentSummary> enrollments;
    private BigDecimal totalMonthlyPremium;
    private BigDecimal totalEmployerContribution;
    private BigDecimal totalEmployeeContribution;
    private Integer totalDependents;
    private LocalDate nextOpenEnrollmentDate;
    private List<String> availableForEnrollment;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnrollmentSummary {
        private String enrollmentId;
        private String planName;
        private String planType;
        private String coverageLevel;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;
        private String status;
        private BigDecimal monthlyPremium;
        private Integer dependentCount;
        private Boolean isActive;
    }
}

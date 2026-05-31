package com.gogidix.hr.benefitsadministration.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for eligibility check
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityCheckResponse {

    private String employeeId;
    private String planId;
    private Boolean isEligible;
    private LocalDate effectiveDate;
    private EligibilityDetails eligibilityDetails;
    private List<String> requirements;
    private List<String> missingRequirements;
    private String ineligibilityReason;
    private LocalDate eligibilityDate;
    private Boolean isWithinEnrollmentWindow;
    private LocalDate enrollmentWindowStart;
    private LocalDate enrollmentWindowEnd;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EligibilityDetails {
        private Boolean meetsAgeRequirement;
        private Boolean meetsTenureRequirement;
        private Boolean meetsEmploymentTypeRequirement;
        private Boolean meetsHoursRequirement;
        private Integer tenureDays;
        private String employmentType;
        private Double hoursPerWeek;
        private Boolean isFullTime;
        private LocalDate hireDate;
        private String jobGrade;
        private String department;
    }
}

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
 * Response DTO for enrollment confirmation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentConfirmationResponse {

    private String enrollmentId;
    private String employeeId;
    private String employeeName;
    private String planId;
    private String planName;
    private String coverageLevel;
    private EnrollmentStatus status;
    private LocalDate effectiveDate;
    private String confirmationNumber;
    private LocalDateTime confirmedAt;
    private Boolean requiresApproval;
    private Boolean requiresEvidence;
    private List<String> requiredEvidence;
    private String nextSteps;
    private String message;
    private Boolean isSuccessful;
    private List<String> warnings;
}

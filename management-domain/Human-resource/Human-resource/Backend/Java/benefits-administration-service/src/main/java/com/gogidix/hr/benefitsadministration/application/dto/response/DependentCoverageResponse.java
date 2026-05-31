package com.gogidix.hr.benefitsadministration.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response DTO for dependent coverage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependentCoverageResponse {

    private String id;
    private String enrollmentId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String relationship;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private Boolean isStudent;
    private Boolean isDisabled;
    private String ssnLast4;
    private LocalDate effectiveDate;
    private LocalDate endDate;
    private Boolean isActive;
    private String status;
    private String notes;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}

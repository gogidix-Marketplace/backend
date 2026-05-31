package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for validating benefit eligibility
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateEligibilityRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    private LocalDate hireDate;

    private LocalDate terminationDate;

    @Size(max = 50, message = "Employment status must not exceed 50 characters")
    private String employmentStatus;

    @Size(max = 50, message = "Employment type must not exceed 50 characters")
    private String employmentType;

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department;

    @Size(max = 100, message = "Job grade must not exceed 100 characters")
    private String jobGrade;

    private Double hoursPerWeek;

    private Boolean isFullTime;

    private LocalDate effectiveDate;

    private Integer age;

    @Size(max = 10, message = "Gender must not exceed 10 characters")
    private String gender;

    private LocalDate dateOfBirth;
}

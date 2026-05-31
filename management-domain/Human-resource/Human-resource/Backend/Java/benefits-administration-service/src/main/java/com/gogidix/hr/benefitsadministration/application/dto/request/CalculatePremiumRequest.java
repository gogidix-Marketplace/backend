package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for calculating benefit premium
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculatePremiumRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotBlank(message = "Coverage level is required")
    private String coverageLevel;

    @NotNull(message = "Number of dependents is required")
    @Min(value = 0, message = "Number of dependents must be non-negative")
    private Integer numberOfDependents;

    private String coverageOptionCode;

    @Size(max = 100, message = "Employee age group must not exceed 100 characters")
    private String employeeAgeGroup;

    private Boolean isSmoker;

    @Size(max = 100, message = "Gender must not exceed 100 characters")
    private String gender;

    @Size(max = 200, message = "Salary band must not exceed 200 characters")
    private String salaryBand;

    private LocalDate effectiveDate;

    @Size(max = 100, message = "Postal code must not exceed 100 characters")
    private String postalCode;

    private Boolean includeSpouse;

    @Min(value = 0, message = "Number of children must be non-negative")
    private Integer numberOfChildren;
}

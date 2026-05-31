package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for enrolling an employee in benefits
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollEmployeeRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @NotBlank(message = "Coverage level is required")
    private String coverageLevel;

    private String coverageOptionCode;

    private Boolean autoEnroll;

    private String enrollmentSource;
}

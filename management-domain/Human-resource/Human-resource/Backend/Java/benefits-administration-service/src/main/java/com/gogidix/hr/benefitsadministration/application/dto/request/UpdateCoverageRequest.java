package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for updating benefit coverage
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCoverageRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @NotBlank(message = "New coverage level is required")
    private String newCoverageLevel;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    private String newCoverageOptionCode;

    @Size(max = 1000, message = "Reason must not exceed 1000 characters")
    private String reason;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}

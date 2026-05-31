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
 * Request DTO for removing a dependent from benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveDependentRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @NotBlank(message = "Dependent ID is required")
    private String dependentId;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @NotBlank(message = "Removal reason is required")
    @Size(max = 500, message = "Removal reason must not exceed 500 characters")
    private String removalReason;
}

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
 * Request DTO for canceling benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelEnrollmentRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @NotBlank(message = "Cancellation reason is required")
    @Size(max = 1000, message = "Cancellation reason must not exceed 1000 characters")
    private String cancellationReason;

    @NotNull(message = "Effective date is required")
    private LocalDate effectiveDate;

    @Size(max = 100, message = "Cancelled by must not exceed 100 characters")
    private String cancelledBy;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}

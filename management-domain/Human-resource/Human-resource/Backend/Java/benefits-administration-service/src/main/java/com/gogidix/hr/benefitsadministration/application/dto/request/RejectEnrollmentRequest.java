package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for rejecting benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectEnrollmentRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @NotBlank(message = "Rejection reason is required")
    @Size(max = 1000, message = "Rejection reason must not exceed 1000 characters")
    private String rejectionReason;

    @Size(max = 100, message = "Rejected by must not exceed 100 characters")
    private String rejectedBy;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;
}

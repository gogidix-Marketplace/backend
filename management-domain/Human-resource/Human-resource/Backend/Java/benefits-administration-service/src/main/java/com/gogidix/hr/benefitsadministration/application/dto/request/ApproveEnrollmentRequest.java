package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for approving benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveEnrollmentRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @Size(max = 1000, message = "Comments must not exceed 1000 characters")
    private String comments;

    @Size(max = 100, message = "Approved by must not exceed 100 characters")
    private String approvedBy;
}

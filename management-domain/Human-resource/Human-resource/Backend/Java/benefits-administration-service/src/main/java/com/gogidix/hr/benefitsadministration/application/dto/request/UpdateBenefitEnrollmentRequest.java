package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.domain.enums.EnrollmentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for updating a benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBenefitEnrollmentRequest {

    private String coverageLevel;

    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    private EnrollmentStatus status;

    @Valid
    private List<CreateBenefitEnrollmentRequest.DependentRequest> dependents;

    private String coverageOptionCode;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private List<String> evidenceDocuments;
}

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
 * Request DTO for creating a benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBenefitEnrollmentRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Coverage level is required")
    private String coverageLevel;

    @NotNull(message = "Effective date is required")
    @FutureOrPresent(message = "Effective date must be today or in the future")
    private LocalDate effectiveDate;

    private LocalDate expiryDate;

    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Valid
    private List<DependentRequest> dependents;

    private String coverageOptionCode;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private Boolean waiveCoverage;

    @Size(max = 500, message = "Waival reason must not exceed 500 characters")
    private String waiverReason;

    private List<String> evidenceDocuments;

    @AssertTrue(message = "Expiry date must be after effective date")
    private boolean isDateRangeValid() {
        if (effectiveDate == null || expiryDate == null) {
            return true;
        }
        return expiryDate.isAfter(effectiveDate);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DependentRequest {
        @NotBlank(message = "Dependent first name is required")
        private String firstName;

        @NotBlank(message = "Dependent last name is required")
        private String lastName;

        @NotBlank(message = "Relationship is required")
        private String relationship;

        private LocalDate dateOfBirth;

        @NotBlank(message = "Gender is required")
        private String gender;

        @Size(max = 500, message = "Address must not exceed 500 characters")
        private String address;

        private Boolean isStudent;

        private Boolean isDisabled;

        private String ssnLast4;

        @Size(max = 1000, message = "Notes must not exceed 1000 characters")
        private String notes;
    }
}

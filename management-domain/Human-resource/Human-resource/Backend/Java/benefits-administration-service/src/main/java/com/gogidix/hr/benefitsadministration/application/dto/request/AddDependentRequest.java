package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request DTO for adding a dependent to benefit enrollment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDependentRequest {

    @NotBlank(message = "Enrollment ID is required")
    private String enrollmentId;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Relationship is required")
    @Size(max = 50, message = "Relationship must not exceed 50 characters")
    private String relationship;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    @Size(max = 20, message = "Gender must not exceed 20 characters")
    private String gender;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    private Boolean isStudent;

    private Boolean isDisabled;

    @Size(max = 10, message = "SSN last 4 must not exceed 10 characters")
    private String ssnLast4;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}

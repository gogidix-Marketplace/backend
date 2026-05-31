package com.gogidix.hr.benefitsadministration.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating a benefit provider
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBenefitProviderRequest {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Provider code is required")
    @Size(max = 50, message = "Provider code must not exceed 50 characters")
    private String providerCode;

    @NotBlank(message = "Provider name is required")
    @Size(max = 200, message = "Provider name must not exceed 200 characters")
    private String providerName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Contact email must be valid")
    @Size(max = 200, message = "Email must not exceed 200 characters")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Size(max = 50, message = "Phone must not exceed 50 characters")
    private String contactPhone;

    @Size(max = 500, message = "Website must not exceed 500 characters")
    private String website;

    @Size(max = 1000, message = "Address must not exceed 1000 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 3, message = "Country code must be 2-3 characters")
    private String countryCode;

    @NotNull(message = "Contract start date is required")
    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    @Size(max = 100, message = "Account number must not exceed 100 characters")
    private String accountNumber;

    @Size(max = 50, message = "Payment terms must not exceed 50 characters")
    private String paymentTerms;

    @NotNull(message = "Is active is required")
    @Builder.Default
    private Boolean isActive = true;

    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    private List<String> supportedPlanTypes;

    @Size(max = 500, message = "Claims contact email must not exceed 500 characters")
    private String claimsContactEmail;

    @Size(max = 50, message = "Claims contact phone must not exceed 50 characters")
    private String claimsContactPhone;

    @Size(max = 500, message = "Claims portal URL must not exceed 500 characters")
    private String claimsPortalUrl;

    @AssertTrue(message = "Contract end date must be after contract start date")
    private boolean isContractDateRangeValid() {
        if (contractStartDate == null || contractEndDate == null) {
            return true;
        }
        return contractEndDate.isAfter(contractStartDate);
    }
}

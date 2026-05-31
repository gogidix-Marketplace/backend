package com.gogidix.hr.benefitsadministration.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for benefit provider
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BenefitProviderResponse {

    private String id;
    private String tenantId;
    private String providerCode;
    private String providerName;
    private String description;
    private String contactEmail;
    private String contactPhone;
    private String website;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String accountNumber;
    private String paymentTerms;
    private Boolean isActive;
    private String notes;
    private List<String> supportedPlanTypes;
    private String claimsContactEmail;
    private String claimsContactPhone;
    private String claimsPortalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
}

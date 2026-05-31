package com.gogidix.hr.benefitsadministration.domain.model;

import com.gogidix.hr.benefitsadministration.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Domain model for Benefit Provider
 * Represents an insurance or benefits provider
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BenefitProvider extends BaseEntity {

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
}

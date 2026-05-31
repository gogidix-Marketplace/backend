package com.gogidix.finance.accountspayable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.accountspayable.domain.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Vendor Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponseDto {

    private String id;

    private String vendorId;

    private String tenantId;

    private String vendorCode;

    private String vendorName;

    private VendorTypeDto vendorType;

    private String taxId;

    private String currency;

    private String paymentTerms;

    private Integer paymentDays;

    private String contactPerson;

    private String email;

    private String phone;

    private String website;

    private AddressDto billingAddress;

    private AddressDto shippingAddress;

    private VendorStatusDto status;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant activatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant deactivatedAt;

    private String deactivationReason;

    private String bankName;

    private String bankAccountType;

    private LocalDate creditLimit;

    private String notes;

    private List<String> tags;

    private String parentVendorId;

    private Boolean isPreferredVendor;

    private Double discountPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validUntil;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum VendorTypeDto {
        INDIVIDUAL,
        CORPORATION,
        PARTNERSHIP,
        LLC,
        NON_PROFIT,
        GOVERNMENT,
        FOREIGN_ENTITY
    }

    public enum VendorStatusDto {
        ACTIVE,
        INACTIVE,
        PENDING_APPROVAL,
        SUSPENDED,
        BLACKLISTED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String addressLine1;
        private String addressLine2;
    }
}

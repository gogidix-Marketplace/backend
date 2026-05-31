package com.gogidix.sales.crm.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Customer Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {

    private String id;
    private String customerId;
    private String tenantId;
    private String accountNumber;
    private String companyName;
    private String industry;
    private CustomerSegmentDto segment;
    private CustomerLifecycleStageDto lifecycleStage;
    private String website;
    private String description;
    private Integer employeeCount;
    private Double annualRevenue;
    private String leadSource;
    private LocalDate leadDate;
    private LocalDate convertedDate;
    private String ownerId;
    private String ownerName;
    private String territory;
    private AddressDto billingAddress;
    private AddressDto shippingAddress;
    private String phoneNumber;
    private String email;
    private Boolean isActive;
    private LocalDate lastContactDate;
    private LocalDate nextFollowUpDate;
    private Integer totalInteractions;
    private Double totalDealValue;
    private Integer openDealsCount;
    private String parentAccountId;
    private List<String> childAccountIds;
    private AccountTypeDto accountType;
    private String taxId;
    private String paymentTerms;
    private String currency;
    private Double creditLimit;
    private List<String> tags;
    private String notes;
    private Integer satisfactionScore;
    private LocalDate churnDate;
    private String churnReason;
    private Instant createdAt;
    private Instant updatedAt;

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
    }

    public enum CustomerSegmentDto {
        ENTERPRISE,
        MID_MARKET,
        SMALL_BUSINESS,
        STARTUP
    }

    public enum CustomerLifecycleStageDto {
        LEAD,
        PROSPECT,
        QUALIFIED_LEAD,
        OPPORTUNITY,
        CUSTOMER,
        CHURNED
    }

    public enum AccountTypeDto {
        STRATEGIC,
        ENTERPRISE,
        MID_MARKET,
        SMALL_BUSINESS,
        PARTNER
    }
}

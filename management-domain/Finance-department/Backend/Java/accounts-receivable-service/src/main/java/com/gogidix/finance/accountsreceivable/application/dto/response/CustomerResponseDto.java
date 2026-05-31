package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
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

    private String customerCode;

    private String customerName;

    private CustomerTypeDto customerType;

    private String email;

    private String phone;

    private String website;

    private String taxId;

    private String taxRegistrationNumber;

    private String billingAddressLine1;

    private String billingAddressLine2;

    private String billingCity;

    private String billingState;

    private String billingPostalCode;

    private String billingCountry;

    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingCity;

    private String shippingState;

    private String shippingPostalCode;

    private String shippingCountry;

    private String currency;

    private String paymentTerms;

    private Integer creditLimit;

    private Integer creditDays;

    private String salesRepresentative;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant customerSince;

    private CustomerStatusDto status;

    private String industry;

    private String notes;

    private String defaultPaymentMethod;

    private Boolean allowCredit;

    private Boolean sendElectronicInvoices;

    private String invoiceDeliveryEmail;

    private String parentCustomerId;

    private Boolean isParentCustomer;

    private BigDecimal outstandingBalance;

    private BigDecimal creditUsed;

    private BigDecimal availableCredit;

    private Integer overdueInvoicesCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastPaymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastInvoiceDate;

    private BigDecimal totalPurchases;

    private Integer totalInvoicesIssued;

    private String assignedCollector;

    private CollectionStageDto collectionStage;

    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum CustomerTypeDto {
        INDIVIDUAL, BUSINESS, GOVERNMENT, NON_PROFIT, RESALE, INTERNATIONAL
    }

    public enum CustomerStatusDto {
        ACTIVE, INACTIVE, SUSPENDED, ON_HOLD, PENDING_APPROVAL, BLOCKED
    }

    public enum CollectionStageDto {
        CURRENT, SOON_DUE, OVERDUE_1_30, OVERDUE_31_60, OVERDUE_61_90, OVERDUE_90_PLUS, COLLECTIONS
    }
}

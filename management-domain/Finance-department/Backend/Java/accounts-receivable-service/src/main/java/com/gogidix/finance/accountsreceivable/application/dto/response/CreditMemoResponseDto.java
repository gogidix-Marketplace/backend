package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Credit Memo Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditMemoResponseDto {

    private String id;

    private String creditMemoId;

    private String tenantId;

    private String creditMemoNumber;

    private String customerId;

    private String customerName;

    private CreditMemoTypeDto creditMemoType;

    private CreditMemoStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creditMemoDate;

    private String referenceInvoiceId;

    private String referenceInvoiceNumber;

    private BigDecimal totalAmount;

    private BigDecimal amountUsed;

    private BigDecimal balanceRemaining;

    private String currency;

    private String reason;

    private String description;

    private String notes;

    private String salesperson;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    private Boolean autoApply;

    private String taxCode;

    private BigDecimal taxAmount;

    private Boolean taxInclusive;

    private List<CreditMemoLineItemDto> lineItems;

    private List<CreditMemoApplicationDto> applications;

    private String parentId;

    private Boolean isReversal;

    private List<String> tags;

    private String projectId;

    private String departmentId;

    private String locationId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant viewedAt;

    private String customerIdRef;

    private String billingAddressLine1;

    private String billingAddressLine2;

    private String billingCity;

    private String billingState;

    private String billingPostalCode;

    private String billingCountry;

    private String purchaseOrderNumber;

    private String vendorCreditNumber;

    private Boolean keepDiscount;

    private BigDecimal discountAmount;

    private String exchangeRate;

    private String baseCurrency;

    private BigDecimal baseCurrencyAmount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum CreditMemoTypeDto {
        SALES_RETURN, PRICE_ADJUSTMENT, PROMOTION, WRITE_OFF, DISCOUNT, DAMAGE,
        LOST_SHIPMENT, CANCELLATION, GENERAL_CREDIT, REBILLING
    }

    public enum CreditMemoStatusDto {
        DRAFT, ISSUED, APPLIED, PARTIALLY_APPLIED, FULLY_APPLIED, EXPIRED, VOID, CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditMemoLineItemDto {
        private String lineItemId;
        private String itemId;
        private String itemCode;
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal lineTotal;
        private String accountCode;
        private String taxCode;
        private String itemType;
        private String reason;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditMemoApplicationDto {
        private String applicationId;
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal appliedAmount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate applicationDate;
        private String notes;
        private String appliedBy;
    }
}

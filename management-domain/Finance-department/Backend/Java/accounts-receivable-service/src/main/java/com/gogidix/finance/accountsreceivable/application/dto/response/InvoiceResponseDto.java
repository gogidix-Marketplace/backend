package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private String id;

    private String invoiceId;

    private String tenantId;

    private String customerId;

    private String customerName;

    private String customerEmail;

    private String invoiceNumber;

    private InvoiceTypeDto invoiceType;

    private InvoiceStatusDto status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate salesDate;

    private String purchaseOrderNumber;

    private String currency;

    private BigDecimal subtotal;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal shippingAmount;

    private BigDecimal totalAmount;

    private BigDecimal amountPaid;

    private BigDecimal balanceDue;

    private List<InvoiceLineItemDto> lineItems;

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

    private String paymentTerms;

    private String notes;

    private String internalNotes;

    private String salesperson;

    private String projectId;

    private String departmentId;

    private String locationId;

    private String templateId;

    private Boolean taxInclusive;

    private Boolean taxRegistered;

    private String taxCode;

    private BigDecimal taxRate;

    private String discountCode;

    private BigDecimal discountRate;

    private String shippingMethod;

    private String trackingNumber;

    private String recurringInvoiceId;

    private Boolean isRecurring;

    private String parentId;

    private Boolean isCreditNote;

    private String originalInvoiceId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastReminderSent;

    private Integer reminderCount;

    private List<PaymentDto> payments;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant sentAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant viewedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String approvedBy;

    private String rejectionReason;

    private String currencyExchangeRate;

    private String baseCurrency;

    private BigDecimal baseCurrencyAmount;

    private List<String> tags;

    private String customerReference;

    private String groupId;

    private Boolean applyFinanceCharge;

    private BigDecimal financeChargeRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate financeChargeAppliedDate;

    private Integer daysOverdue;

    private BigDecimal writeOffAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate writeOffDate;

    private String writeOffReason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum InvoiceTypeDto {
        STANDARD, RECURRING, CREDIT_NOTE, DEBIT_NOTE, PROFORMA, COMMERCIAL, INTER_COMPANY
    }

    public enum InvoiceStatusDto {
        DRAFT, SENT, VIEWED, PARTIALLY_PAID, PAID, OVERDUE, WRITE_OFF, CANCELLED, VOID
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItemDto {
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
        private String serviceStartDate;
        private String serviceEndDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDto {
        private String paymentId;
        private String transactionId;
        private BigDecimal amount;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant paidAt;
        private String paymentMethod;
        private String notes;
    }
}

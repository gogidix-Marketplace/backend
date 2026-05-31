package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Credit Memo Domain Entity
 * Represents credit notes issued to customers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ar_credit_memos")
public class CreditMemo extends BaseEntity {

    private String creditMemoId;

    private String tenantId;

    private String creditMemoNumber;

    private String customerId;

    private String customerName;

    private CreditMemoType creditMemoType;

    private CreditMemoStatus status;

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

    private Instant approvedAt;

    private LocalDate expirationDate;

    private Boolean autoApply;

    private String taxCode;

    private BigDecimal taxAmount;

    private Boolean taxInclusive;

    @Builder.Default
    private List<CreditMemoLineItem> lineItems = new ArrayList<>();

    @Builder.Default
    private List<CreditMemoApplication> applications = new ArrayList<>();

    private String parentId;

    private Boolean isReversal;

    private List<String> tags;

    private String projectId;

    private String departmentId;

    private String locationId;

    private Instant sentAt;

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

    public enum CreditMemoType {
        SALES_RETURN,
        PRICE_ADJUSTMENT,
        PROMOTION,
        WRITE_OFF,
        DISCOUNT,
        DAMAGE,
        LOST_SHIPMENT,
        CANCELLATION,
        GENERAL_CREDIT,
        REBILLING
    }

    public enum CreditMemoStatus {
        DRAFT,
        ISSUED,
        APPLIED,
        PARTIALLY_APPLIED,
        FULLY_APPLIED,
        EXPIRED,
        VOID,
        CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditMemoLineItem {
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
    public static class CreditMemoApplication {
        private String applicationId;
        private String invoiceId;
        private String invoiceNumber;
        private BigDecimal appliedAmount;
        private LocalDate applicationDate;
        private String notes;
        private String appliedBy;
    }

    /**
     * Creates a new credit memo
     */
    public static CreditMemo create(String tenantId, String customerId, String customerName,
                                      CreditMemoType creditMemoType, BigDecimal amount,
                                      String currency, LocalDate creditMemoDate, String reason) {
        CreditMemo creditMemo = CreditMemo.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .customerName(customerName)
            .creditMemoType(creditMemoType)
            .totalAmount(amount)
            .currency(currency)
            .creditMemoDate(creditMemoDate)
            .reason(reason)
            .status(CreditMemoStatus.DRAFT)
            .amountUsed(BigDecimal.ZERO)
            .balanceRemaining(amount)
            .autoApply(false)
            .keepDiscount(true)
            .lineItems(new ArrayList<>())
            .applications(new ArrayList<>())
            .tags(new ArrayList<>())
            .build();

        return creditMemo;
    }

    /**
     * Issues the credit memo
     */
    public void issue() {
        if (this.status != CreditMemoStatus.DRAFT) {
            throw new IllegalStateException("Can only issue draft credit memos");
        }

        validateCreditMemo();

        this.status = CreditMemoStatus.ISSUED;
        this.sentAt = Instant.now();
    }

    /**
     * Applies credit memo to invoice
     */
    public void applyToInvoice(String invoiceId, String invoiceNumber, BigDecimal amount, String appliedBy) {
        if (this.status != CreditMemoStatus.ISSUED && this.status != CreditMemoStatus.PARTIALLY_APPLIED) {
            throw new IllegalStateException("Can only apply issued or partially applied credit memos");
        }

        if (this.balanceRemaining.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient credit balance");
        }

        if (LocalDate.now().isAfter(this.expirationDate) && this.expirationDate != null) {
            throw new IllegalStateException("Credit memo has expired");
        }

        CreditMemoApplication application = CreditMemoApplication.builder()
            .applicationId(java.util.UUID.randomUUID().toString())
            .invoiceId(invoiceId)
            .invoiceNumber(invoiceNumber)
            .appliedAmount(amount)
            .applicationDate(LocalDate.now())
            .appliedBy(appliedBy)
            .build();

        this.applications.add(application);
        this.amountUsed = this.amountUsed.add(amount);
        this.balanceRemaining = this.totalAmount.subtract(this.amountUsed);

        if (this.balanceRemaining.compareTo(BigDecimal.ZERO) == 0) {
            this.status = CreditMemoStatus.FULLY_APPLIED;
        } else {
            this.status = CreditMemoStatus.PARTIALLY_APPLIED;
        }
    }

    /**
     * Voids the credit memo
     */
    public void voidCreditMemo(String reason) {
        if (this.status == CreditMemoStatus.FULLY_APPLIED) {
            throw new IllegalStateException("Cannot void fully applied credit memo");
        }

        this.status = CreditMemoStatus.VOID;
        this.notes = reason;
    }

    /**
     * Cancels the credit memo
     */
    public void cancel(String reason) {
        if (this.status == CreditMemoStatus.FULLY_APPLIED || this.status == CreditMemoStatus.APPLIED) {
            throw new IllegalStateException("Cannot cancel applied credit memo");
        }

        this.status = CreditMemoStatus.CANCELLED;
        this.notes = reason;
    }

    /**
     * Adds line item to credit memo
     */
    public void addLineItem(CreditMemoLineItem lineItem) {
        if (this.status != CreditMemoStatus.DRAFT) {
            throw new IllegalStateException("Can only add line items to draft credit memos");
        }
        if (this.lineItems == null) {
            this.lineItems = new ArrayList<>();
        }
        this.lineItems.add(lineItem);
    }

    /**
     * Removes line item from credit memo
     */
    public void removeLineItem(String lineItemId) {
        if (this.status != CreditMemoStatus.DRAFT) {
            throw new IllegalStateException("Can only remove line items from draft credit memos");
        }
        if (this.lineItems != null) {
            this.lineItems.removeIf(item -> item.getLineItemId().equals(lineItemId));
        }
    }

    /**
     * Checks if credit memo is expired
     */
    public boolean isExpired() {
        return this.expirationDate != null && LocalDate.now().isAfter(this.expirationDate);
    }

    /**
     * Checks if credit memo can be applied
     */
    public boolean canApply() {
        return (this.status == CreditMemoStatus.ISSUED || this.status == CreditMemoStatus.PARTIALLY_APPLIED) &&
            !isExpired() && this.balanceRemaining.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Adds a tag to the credit memo
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Removes a tag from the credit memo
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    private void validateCreditMemo() {
        if (this.totalAmount == null || this.totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new com.gogidix.finance.accountsreceivable.shared.exception.ValidationException(
                "Total amount must be positive");
        }
        if (this.customerId == null || this.customerId.isBlank()) {
            throw new com.gogidix.finance.accountsreceivable.shared.exception.ValidationException(
                "Customer is required");
        }
    }

    public void clearDomainEvents() {
        // No domain events in CreditMemo currently
    }
}

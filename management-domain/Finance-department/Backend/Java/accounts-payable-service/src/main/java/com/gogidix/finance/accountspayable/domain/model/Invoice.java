package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.domain.event.InvoiceCreatedEvent;
import com.gogidix.finance.accountspayable.shared.base.BaseEntity;
import com.gogidix.finance.accountspayable.shared.exception.ValidationException;
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
 * Invoice Domain Entity
 * Multi-tenant invoice management with approval workflow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "invoices")
public class Invoice extends BaseEntity {

    private String invoiceId;

    private String tenantId;

    private String vendorId;

    private String vendorName;

    private String vendorCode;

    private String invoiceNumber;

    private String purchaseOrderNumber;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private LocalDate receivedDate;

    private BigDecimal amount;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;

    private String currency;

    private InvoiceStatus status;

    private String submittedBy;

    private Instant submittedAt;

    private String approvedBy;

    private Instant approvedAt;

    private String rejectionReason;

    private String paymentReference;

    private Instant paidAt;

    private String description;

    private String notes;

    private String internalReference;

    private String department;

    private String costCenter;

    private String projectId;

    private List<InvoiceLineItem> lineItems;

    private List<String> attachments;

    private List<String> tags;

    private Boolean requiresApproval;

    private ApprovalLevel approvalLevel;

    private String glAccount;

    private String taxCode;

    private Boolean taxIncluded;

    private LocalDate discountValidUntil;

    private BigDecimal discountPercentage;

    private String paymentTerms;

    @Builder.Default
    private List<InvoiceCreatedEvent> domainEvents = new ArrayList<>();

    public enum InvoiceStatus {
        DRAFT,
        PENDING,
        APPROVED,
        PAID,
        CANCELLED,
        REJECTED,
        OVERDUE,
        PARTIALLY_PAID
    }

    public enum ApprovalLevel {
        NONE,
        MANAGER,
        FINANCE,
        EXECUTIVE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItem {
        private String lineItemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private String accountCode;
        private String taxCode;
    }

    /**
     * Creates a new invoice
     */
    public static Invoice create(String tenantId, String vendorId, String vendorName,
                                  String invoiceNumber, LocalDate invoiceDate, LocalDate dueDate,
                                  BigDecimal amount, String currency, String submittedBy) {
        Invoice invoice = Invoice.builder()
            .tenantId(tenantId)
            .vendorId(vendorId)
            .vendorName(vendorName)
            .invoiceNumber(invoiceNumber)
            .invoiceDate(invoiceDate)
            .dueDate(dueDate)
            .receivedDate(LocalDate.now())
            .amount(amount)
            .currency(currency)
            .status(InvoiceStatus.DRAFT)
            .submittedBy(submittedBy)
            .lineItems(new ArrayList<>())
            .attachments(new ArrayList<>())
            .tags(new ArrayList<>())
            .requiresApproval(true)
            .approvalLevel(ApprovalLevel.NONE)
            .taxIncluded(false)
            .build();

        invoice.calculateNetAmount();

        invoice.addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(invoice.getInvoiceId())
            .tenantId(tenantId)
            .vendorId(vendorId)
            .invoiceNumber(invoiceNumber)
            .amount(amount)
            .currency(currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_CREATED")
            .build());

        return invoice;
    }

    /**
     * Submits the invoice for approval
     */
    public void submit() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only submit draft invoices");
        }

        validateForSubmission();

        this.status = InvoiceStatus.PENDING;
        this.submittedAt = Instant.now();
        this.approvalLevel = ApprovalLevel.MANAGER;

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_SUBMITTED")
            .build());
    }

    /**
     * Approves the invoice
     */
    public void approve(String approver, ApprovalLevel level) {
        if (!canApprove(level)) {
            throw new IllegalStateException("Cannot approve at this level");
        }

        this.approvedBy = approver;
        this.approvedAt = Instant.now();
        this.approvalLevel = level;

        if (level == ApprovalLevel.EXECUTIVE || level == ApprovalLevel.FINANCE) {
            this.status = InvoiceStatus.APPROVED;
        }

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_APPROVED")
            .build());
    }

    /**
     * Rejects the invoice
     */
    public void reject(String rejecter, String reason) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.PARTIALLY_PAID) {
            throw new IllegalStateException("Cannot reject paid invoices");
        }

        this.status = InvoiceStatus.REJECTED;
        this.approvedBy = rejecter;
        this.rejectionReason = reason;
        this.approvedAt = Instant.now();

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_REJECTED")
            .build());
    }

    /**
     * Marks invoice as paid
     */
    public void markAsPaid(String paymentReference) {
        if (this.status != InvoiceStatus.APPROVED && this.status != InvoiceStatus.PARTIALLY_PAID) {
            throw new IllegalStateException("Can only mark approved invoices as paid");
        }

        this.status = InvoiceStatus.PAID;
        this.paymentReference = paymentReference;
        this.paidAt = Instant.now();

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_PAID")
            .build());
    }

    /**
     * Marks invoice as partially paid
     */
    public void markAsPartiallyPaid(String paymentReference, BigDecimal amountPaid) {
        if (this.status != InvoiceStatus.APPROVED) {
            throw new IllegalStateException("Can only mark approved invoices as partially paid");
        }

        this.status = InvoiceStatus.PARTIALLY_PAID;

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(amountPaid)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_PARTIALLY_PAID")
            .build());
    }

    /**
     * Cancels the invoice
     */
    public void cancel(String reason) {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot cancel paid invoices");
        }

        this.status = InvoiceStatus.CANCELLED;
        this.notes = reason;

        addDomainEvent(InvoiceCreatedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .vendorId(this.vendorId)
            .invoiceNumber(this.invoiceNumber)
            .amount(this.amount)
            .currency(this.currency)
            .timestamp(Instant.now())
            .eventType("INVOICE_CANCELLED")
            .build());
    }

    /**
     * Marks invoice as overdue
     */
    public void markAsOverdue() {
        if (this.status == InvoiceStatus.PENDING || this.status == InvoiceStatus.APPROVED) {
            if (LocalDate.now().isAfter(this.dueDate)) {
                this.status = InvoiceStatus.OVERDUE;

                addDomainEvent(InvoiceCreatedEvent.builder()
                    .invoiceId(this.invoiceId)
                    .tenantId(this.tenantId)
                    .vendorId(this.vendorId)
                    .invoiceNumber(this.invoiceNumber)
                    .amount(this.amount)
                    .currency(this.currency)
                    .timestamp(Instant.now())
                    .eventType("INVOICE_OVERDUE")
                    .build());
            }
        }
    }

    /**
     * Adds a line item
     */
    public void addLineItem(InvoiceLineItem item) {
        if (this.lineItems == null) {
            this.lineItems = new ArrayList<>();
        }
        this.lineItems.add(item);
        calculateNetAmount();
    }

    /**
     * Removes a line item
     */
    public void removeLineItem(String lineItemId) {
        if (this.lineItems != null) {
            this.lineItems.removeIf(item -> item.getLineItemId().equals(lineItemId));
            calculateNetAmount();
        }
    }

    /**
     * Adds a tag
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
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Adds an attachment
     */
    public void addAttachment(String attachmentUrl) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(attachmentUrl);
    }

    /**
     * Calculates net amount
     */
    public void calculateNetAmount() {
        BigDecimal net = this.amount;

        if (this.taxAmount != null) {
            net = net.add(this.taxAmount);
        }

        if (this.discountAmount != null) {
            net = net.subtract(this.discountAmount);
        }

        this.netAmount = net;
    }

    /**
     * Calculates tax amount
     */
    public void calculateTax(BigDecimal taxRate) {
        this.taxAmount = this.amount.multiply(taxRate);
        calculateNetAmount();
    }

    /**
     * Calculates discount amount
     */
    public void calculateDiscount(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
        this.discountAmount = this.amount.multiply(discountPercentage).divide(new BigDecimal("100"));
        calculateNetAmount();
    }

    /**
     * Checks if invoice is overdue
     */
    public boolean isOverdue() {
        return this.status == InvoiceStatus.OVERDUE ||
               (this.status != InvoiceStatus.PAID && LocalDate.now().isAfter(this.dueDate));
    }

    /**
     * Gets days until due date
     */
    public long getDaysUntilDue() {
        return LocalDate.now().until(this.dueDate).getDays();
    }

    private void validateForSubmission() {
        if (this.invoiceNumber == null || this.invoiceNumber.isBlank()) {
            throw new ValidationException("invoiceNumber", "Invoice number is required");
        }
        if (this.amount == null || this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Amount must be positive");
        }
        if (this.dueDate == null) {
            throw new ValidationException("dueDate", "Due date is required");
        }
        if (this.vendorId == null || this.vendorId.isBlank()) {
            throw new ValidationException("vendorId", "Vendor ID is required");
        }
    }

    private boolean canApprove(ApprovalLevel level) {
        return this.status == InvoiceStatus.PENDING ||
               (this.status == InvoiceStatus.APPROVED &&
                this.approvalLevel == ApprovalLevel.MANAGER &&
                (level == ApprovalLevel.FINANCE || level == ApprovalLevel.EXECUTIVE));
    }

    public void addDomainEvent(InvoiceCreatedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}

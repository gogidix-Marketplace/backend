package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.event.InvoiceGeneratedEvent;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice Domain Entity
 * Multi-tenant AR invoice with full lifecycle management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ar_invoices")
public class Invoice extends BaseEntity {

    private String invoiceId;

    private String tenantId;

    private String invoiceNumber;

    private String customerId;

    private String customerName;

    private String customerEmail;

    private InvoiceType invoiceType;

    private InvoiceStatus status;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

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

    @Builder.Default
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

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

    private LocalDate lastReminderSent;

    private Integer reminderCount;

    @Builder.Default
    private List<InvoiceGeneratedEvent> domainEvents = new ArrayList<>();

    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    private Instant sentAt;

    private Instant viewedAt;

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

    private LocalDate financeChargeAppliedDate;

    private Integer daysOverdue;

    private BigDecimal writeOffAmount;

    private LocalDate writeOffDate;

    private String writeOffReason;

    public enum InvoiceType {
        STANDARD,
        RECURRING,
        CREDIT_NOTE,
        DEBIT_NOTE,
        PROFORMA,
        COMMERCIAL,
        INTER_COMPANY
    }

    public enum InvoiceStatus {
        DRAFT,
        SENT,
        VIEWED,
        PARTIALLY_PAID,
        PAID,
        OVERDUE,
        WRITE_OFF,
        CANCELLED,
        VOID
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItem {
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

    /**
     * Creates a new invoice
     */
    public static Invoice create(String tenantId, String customerId, String customerName,
                                  String invoiceNumber, InvoiceType invoiceType,
                                  LocalDate invoiceDate, LocalDate dueDate,
                                  String currency, List<InvoiceLineItem> lineItems) {
        Invoice invoice = Invoice.builder()
            .tenantId(tenantId)
            .customerId(customerId)
            .customerName(customerName)
            .invoiceNumber(invoiceNumber)
            .invoiceType(invoiceType)
            .invoiceDate(invoiceDate)
            .dueDate(dueDate)
            .currency(currency)
            .lineItems(lineItems)
            .status(InvoiceStatus.DRAFT)
            .amountPaid(BigDecimal.ZERO)
            .reminderCount(0)
            .applyFinanceCharge(false)
            .tags(new ArrayList<>())
            .build();

        invoice.calculateTotals();

        invoice.addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(invoice.getInvoiceId())
            .tenantId(tenantId)
            .customerId(customerId)
            .invoiceNumber(invoiceNumber)
            .totalAmount(invoice.getTotalAmount())
            .currency(currency)
            .dueDate(dueDate != null ? dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null)
            .eventType("INVOICE_CREATED")
            .timestamp(Instant.now())
            .build());

        return invoice;
    }

    /**
     * Sends invoice to customer
     */
    public void send() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only send draft invoices");
        }

        validateInvoice();

        this.status = InvoiceStatus.SENT;
        this.sentAt = Instant.now();

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .totalAmount(this.totalAmount)
            .currency(this.currency)
            .dueDate(this.dueDate != null ? this.dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null)
            .eventType("INVOICE_SENT")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Marks invoice as viewed
     */
    public void markAsViewed() {
        if (this.status == InvoiceStatus.SENT) {
            this.status = InvoiceStatus.VIEWED;
            this.viewedAt = Instant.now();
        }
    }

    /**
     * Records payment on invoice
     */
    public void recordPayment(BigDecimal amount) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED || this.status == InvoiceStatus.VOID) {
            throw new IllegalStateException("Cannot record payment on " + this.status + " invoice");
        }

        this.amountPaid = this.amountPaid.add(amount);
        this.balanceDue = this.totalAmount.subtract(this.amountPaid);

        if (this.balanceDue.compareTo(BigDecimal.ZERO) <= 0) {
            this.status = InvoiceStatus.PAID;
            this.balanceDue = BigDecimal.ZERO;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .totalAmount(this.totalAmount)
            .amountPaid(this.amountPaid)
            .balanceDue(this.balanceDue)
            .currency(this.currency)
            .eventType("PAYMENT_RECORDED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Marks invoice as overdue
     */
    public void markAsOverdue() {
        if (this.status != InvoiceStatus.SENT && this.status != InvoiceStatus.VIEWED &&
            this.status != InvoiceStatus.PARTIALLY_PAID) {
            return;
        }

        this.status = InvoiceStatus.OVERDUE;

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .totalAmount(this.totalAmount)
            .balanceDue(this.balanceDue)
            .currency(this.currency)
            .dueDate(this.dueDate != null ? this.dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null)
            .eventType("INVOICE_OVERDUE")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Writes off invoice
     */
    public void writeOff(BigDecimal amount, String reason) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.WRITE_OFF ||
            this.status == InvoiceStatus.CANCELLED || this.status == InvoiceStatus.VOID) {
            throw new IllegalStateException("Cannot write off " + this.status + " invoice");
        }

        this.status = InvoiceStatus.WRITE_OFF;
        this.writeOffAmount = amount;
        this.writeOffDate = LocalDate.now();
        this.writeOffReason = reason;
        this.balanceDue = this.balanceDue.subtract(amount);

        if (this.balanceDue.compareTo(BigDecimal.ZERO) <= 0) {
            this.balanceDue = BigDecimal.ZERO;
        }

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .totalAmount(this.totalAmount)
            .writeOffAmount(amount)
            .currency(this.currency)
            .eventType("INVOICE_WRITTEN_OFF")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Voids the invoice
     */
    public void voidInvoice() {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot void paid invoice. Use credit note instead.");
        }

        this.status = InvoiceStatus.VOID;

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .eventType("INVOICE_VOIDED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Cancels the invoice
     */
    public void cancel(String reason) {
        if (this.status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot cancel paid invoice");
        }

        this.status = InvoiceStatus.CANCELLED;
        this.rejectionReason = reason;

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .eventType("INVOICE_CANCELLED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Sends payment reminder
     */
    public void sendReminder() {
        this.reminderCount++;
        this.lastReminderSent = LocalDate.now();

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .totalAmount(this.totalAmount)
            .balanceDue(this.balanceDue)
            .currency(this.currency)
            .dueDate(this.dueDate != null ? this.dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant() : null)
            .eventType("PAYMENT_REMINDER_SENT")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Adds line item to invoice
     */
    public void addLineItem(InvoiceLineItem lineItem) {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only add line items to draft invoices");
        }
        if (this.lineItems == null) {
            this.lineItems = new ArrayList<>();
        }
        this.lineItems.add(lineItem);
        calculateTotals();
    }

    /**
     * Removes line item from invoice
     */
    public void removeLineItem(String lineItemId) {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only remove line items from draft invoices");
        }
        if (this.lineItems != null) {
            this.lineItems.removeIf(item -> item.getLineItemId().equals(lineItemId));
            calculateTotals();
        }
    }

    /**
     * Calculates overdue days
     */
    public void calculateDaysOverdue() {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED ||
            this.status == InvoiceStatus.VOID || this.status == InvoiceStatus.DRAFT) {
            this.daysOverdue = 0;
            return;
        }

        if (LocalDate.now().isAfter(this.dueDate)) {
            this.daysOverdue = (int) java.time.temporal.ChronoUnit.DAYS.between(this.dueDate, LocalDate.now());
        } else {
            this.daysOverdue = 0;
        }
    }

    /**
     * Applies finance charge
     */
    public void applyFinanceCharge(BigDecimal rate) {
        if (this.status != InvoiceStatus.OVERDUE) {
            throw new IllegalStateException("Can only apply finance charge to overdue invoices");
        }

        BigDecimal financeCharge = this.balanceDue.multiply(rate.divide(new BigDecimal("100")));
        this.financeChargeRate = rate;
        this.financeChargeAppliedDate = LocalDate.now();
        this.balanceDue = this.balanceDue.add(financeCharge);
        this.totalAmount = this.totalAmount.add(financeCharge);

        addDomainEvent(InvoiceGeneratedEvent.builder()
            .invoiceId(this.invoiceId)
            .tenantId(this.tenantId)
            .customerId(this.customerId)
            .invoiceNumber(this.invoiceNumber)
            .eventType("FINANCE_CHARGE_APPLIED")
            .timestamp(Instant.now())
            .build());
    }

    /**
     * Adds a tag to the invoice
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
     * Removes a tag from the invoice
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    private void calculateTotals() {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        if (this.lineItems != null) {
            for (InvoiceLineItem item : this.lineItems) {
                if (item.getQuantity() != null && item.getUnitPrice() != null) {
                    BigDecimal lineTotal = item.getUnitPrice()
                        .multiply(new BigDecimal(item.getQuantity()));
                    if (item.getDiscountAmount() != null) {
                        lineTotal = lineTotal.subtract(item.getDiscountAmount());
                        totalDiscount = totalDiscount.add(item.getDiscountAmount());
                    }
                    item.setLineTotal(lineTotal);
                    subtotal = subtotal.add(lineTotal);
                }
                if (item.getTaxAmount() != null) {
                    totalTax = totalTax.add(item.getTaxAmount());
                }
            }
        }

        this.subtotal = subtotal;
        this.taxAmount = totalTax;
        this.discountAmount = totalDiscount;
        this.shippingAmount = this.shippingAmount != null ? this.shippingAmount : BigDecimal.ZERO;

        this.totalAmount = subtotal.add(totalTax)
            .add(this.shippingAmount)
            .subtract(totalDiscount);

        this.balanceDue = this.totalAmount.subtract(
            this.amountPaid != null ? this.amountPaid : BigDecimal.ZERO);
    }

    private void validateInvoice() {
        if (this.lineItems == null || this.lineItems.isEmpty()) {
            throw new com.gogidix.finance.accountsreceivable.shared.exception.ValidationException(
                "lineItems", "Invoice must have at least one line item");
        }
        if (this.customerId == null || this.customerId.isBlank()) {
            throw new com.gogidix.finance.accountsreceivable.shared.exception.ValidationException(
                "customerId", "Customer is required");
        }
        if (this.totalAmount == null || this.totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new com.gogidix.finance.accountsreceivable.shared.exception.ValidationException(
                "totalAmount", "Total amount must be positive");
        }
    }

    public void addDomainEvent(InvoiceGeneratedEvent event) {
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

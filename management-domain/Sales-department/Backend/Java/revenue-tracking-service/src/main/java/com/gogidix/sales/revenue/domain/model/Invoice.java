package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice Domain Entity
 * Multi-tenant invoice tracking for revenue reconciliation
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

    private String invoiceNumber;

    private String invoiceType;

    private String customerId;

    private String customerName;

    private String customerBillingAddress;

    private String customerTaxId;

    private BigDecimal subtotalAmount;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private String currency;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private LocalDate paidDate;

    private InvoiceStatus status;

    private PaymentStatus paymentStatus;

    private String paymentMethod;

    private String paymentReference;

    @Builder.Default
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

    @Builder.Default
    private List<InvoicePayment> payments = new ArrayList<>();

    private String contractId;

    private String orderId;

    private String revenueId;

    private String salespersonId;

    private String salespersonName;

    private String department;

    private String territory;

    private String region;

    private String notes;

    private String terms;

    private Integer paymentTermsDays;

    private BigDecimal overdueAmount;

    private Integer daysOverdue;

    private Integer reminderCount;

    private LocalDate lastReminderDate;

    private String parentId;

    private String creditMemoId;

    private BigDecimal creditedAmount;

    private Boolean reconciled;

    private String reconciliationId;

    private LocalDate reconciliationDate;

    public enum InvoiceType {
        STANDARD,
        RECURRING,
        CREDIT_MEMO,
        DEBIT_MEMO,
        PROFORMA,
        PREPAYMENT,
        FINAL,
        INTERIM
    }

    public enum InvoiceStatus {
        DRAFT,
        PENDING,
        SENT,
        VIEWED,
        APPROVED,
        DISPUTED,
        OVERDUE,
        PAID,
        PARTIALLY_PAID,
        WRITE_OFF,
        CANCELLED,
        VOID
    }

    public enum PaymentStatus {
        UNPAID,
        PARTIALLY_PAID,
        PAID,
        OVERDUE,
        SCHEDULED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItem {
        private String lineItemId;
        private String productId;
        private String productName;
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private BigDecimal taxRate;
        private BigDecimal taxAmount;
        private BigDecimal lineTotal;
        private String revenueId;
        private String contractId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoicePayment {
        private String paymentId;
        private LocalDate paymentDate;
        private BigDecimal amount;
        private String paymentMethod;
        private String reference;
        private String notes;
    }

    /**
     * Creates a new invoice
     */
    public static Invoice create(String tenantId, String invoiceNumber, String customerId,
                                  String customerName, BigDecimal subtotalAmount, BigDecimal taxAmount,
                                  BigDecimal discountAmount, BigDecimal totalAmount, String currency,
                                  LocalDate invoiceDate, LocalDate dueDate, String invoiceType) {
        Invoice invoice = Invoice.builder()
                .tenantId(tenantId)
                .invoiceNumber(invoiceNumber)
                .customerId(customerId)
                .customerName(customerName)
                .subtotalAmount(subtotalAmount)
                .taxAmount(taxAmount != null ? taxAmount : BigDecimal.ZERO)
                .discountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO)
                .totalAmount(totalAmount)
                .currency(currency)
                .invoiceDate(invoiceDate)
                .dueDate(dueDate)
                .status(InvoiceStatus.DRAFT)
                .paymentStatus(PaymentStatus.UNPAID)
                .lineItems(new ArrayList<>())
                .payments(new ArrayList<>())
                .reminderCount(0)
                .reconciled(false)
                .invoiceType(invoiceType != null ? invoiceType : InvoiceType.STANDARD.name())
                .build();

        return invoice;
    }

    /**
     * Adds a line item to the invoice
     */
    public void addLineItem(String productId, String productName, String description,
                            Integer quantity, BigDecimal unitPrice, BigDecimal discount,
                            BigDecimal taxRate) {
        BigDecimal lineSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal lineDiscount = discount != null ? discount : BigDecimal.ZERO;
        BigDecimal lineTaxRate = taxRate != null ? taxRate : BigDecimal.ZERO;
        BigDecimal lineTaxAmount = lineSubtotal.subtract(lineDiscount).multiply(lineTaxRate);
        BigDecimal lineTotal = lineSubtotal.subtract(lineDiscount).add(lineTaxAmount);

        InvoiceLineItem lineItem = InvoiceLineItem.builder()
                .lineItemId(java.util.UUID.randomUUID().toString())
                .productId(productId)
                .productName(productName)
                .description(description)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .discount(lineDiscount)
                .taxRate(lineTaxRate)
                .taxAmount(lineTaxAmount)
                .lineTotal(lineTotal)
                .build();

        this.lineItems.add(lineItem);
        recalculateTotals();
    }

    /**
     * Sends the invoice
     */
    public void send() {
        if (this.status != InvoiceStatus.DRAFT && this.status != InvoiceStatus.PENDING) {
            throw new IllegalStateException("Can only send draft or pending invoices");
        }
        this.status = InvoiceStatus.SENT;
    }

    /**
     * Marks invoice as viewed
     */
    public void markAsViewed() {
        if (this.status != InvoiceStatus.SENT) {
            throw new IllegalStateException("Can only mark sent invoices as viewed");
        }
        this.status = InvoiceStatus.VIEWED;
    }

    /**
     * Approves the invoice
     */
    public void approve() {
        if (this.status != InvoiceStatus.DRAFT && this.status != InvoiceStatus.PENDING) {
            throw new IllegalStateException("Can only approve draft or pending invoices");
        }
        this.status = InvoiceStatus.APPROVED;
    }

    /**
     * Records a payment
     */
    public void recordPayment(BigDecimal amount, String paymentMethod, String reference) {
        if (amount.compareTo(this.getOutstandingAmount()) > 0) {
            throw new IllegalArgumentException("Payment amount exceeds outstanding amount");
        }

        InvoicePayment payment = InvoicePayment.builder()
                .paymentId(java.util.UUID.randomUUID().toString())
                .paymentDate(LocalDate.now())
                .amount(amount)
                .paymentMethod(paymentMethod)
                .reference(reference)
                .build();

        this.payments.add(payment);

        BigDecimal totalPaid = getTotalPaid();
        if (totalPaid.compareTo(this.totalAmount) >= 0) {
            this.status = InvoiceStatus.PAID;
            this.paymentStatus = PaymentStatus.PAID;
            this.paidDate = LocalDate.now();
            this.paymentMethod = paymentMethod;
            this.paymentReference = reference;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
            this.paymentStatus = PaymentStatus.PARTIALLY_PAID;
        }
    }

    /**
     * Marks invoice as overdue
     */
    public void markAsOverdue() {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED) {
            return;
        }
        if (LocalDate.now().isAfter(this.dueDate)) {
            this.status = InvoiceStatus.OVERDUE;
            this.paymentStatus = PaymentStatus.OVERDUE;
            this.daysOverdue = (int) java.time.temporal.ChronoUnit.DAYS.between(this.dueDate, LocalDate.now());
        }
    }

    /**
     * Increments reminder count
     */
    public void incrementReminder() {
        this.reminderCount++;
        this.lastReminderDate = LocalDate.now();
    }

    /**
     * Disputes the invoice
     */
    public void dispute(String reason) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Cannot dispute paid or cancelled invoices");
        }
        this.status = InvoiceStatus.DISPUTED;
        this.notes = reason;
    }

    /**
     * Writes off the invoice
     */
    public void writeOff(String reason) {
        if (this.status == InvoiceStatus.PAID || this.status == InvoiceStatus.CANCELLED) {
            throw new IllegalStateException("Cannot write off paid or cancelled invoices");
        }
        this.status = InvoiceStatus.WRITE_OFF;
        this.notes = reason;
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
    }

    /**
     * Voids the invoice
     */
    public void voidInvoice(String reason) {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new IllegalStateException("Can only void draft invoices");
        }
        this.status = InvoiceStatus.VOID;
        this.notes = reason;
    }

    /**
     * Marks invoice as reconciled
     */
    public void markAsReconciled(String reconciliationId) {
        this.reconciled = true;
        this.reconciliationId = reconciliationId;
        this.reconciliationDate = LocalDate.now();
    }

    /**
     * Gets total paid amount
     */
    public BigDecimal getTotalPaid() {
        return this.payments.stream()
                .map(InvoicePayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Gets outstanding amount
     */
    public BigDecimal getOutstandingAmount() {
        return this.totalAmount.subtract(getTotalPaid());
    }

    /**
     * Recalculates invoice totals
     */
    public void recalculateTotals() {
        this.subtotalAmount = this.lineItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.taxAmount = this.lineItems.stream()
                .map(InvoiceLineItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lineItemsTotal = this.lineItems.stream()
                .map(InvoiceLineItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = lineItemsTotal;
    }

    /**
     * Checks if invoice is overdue
     */
    public boolean isOverdue() {
        return this.status == InvoiceStatus.OVERDUE
                || (this.status != InvoiceStatus.PAID
                && this.status != InvoiceStatus.CANCELLED
                && LocalDate.now().isAfter(this.dueDate));
    }

    /**
     * Gets days until due
     */
    public Integer getDaysUntilDue() {
        if (this.dueDate == null) {
            return null;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), this.dueDate);
        return (int) days;
    }

    /**
     * Applies credit memo
     */
    public void applyCreditMemo(String creditMemoId, BigDecimal amount) {
        if (amount.compareTo(getOutstandingAmount()) > 0) {
            throw new IllegalArgumentException("Credit amount exceeds outstanding amount");
        }
        this.creditMemoId = creditMemoId;
        this.creditedAmount = amount;

        BigDecimal newOutstanding = getOutstandingAmount().subtract(amount);
        if (newOutstanding.compareTo(BigDecimal.ZERO) == 0) {
            this.status = InvoiceStatus.PAID;
            this.paymentStatus = PaymentStatus.PAID;
        }
    }

    /**
     * Gets line item count
     */
    public int getLineItemCount() {
        return this.lineItems != null ? this.lineItems.size() : 0;
    }

    /**
     * Gets payment count
     */
    public int getPaymentCount() {
        return this.payments != null ? this.payments.size() : 0;
    }
}

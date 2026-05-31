package com.gogidix.platform.subscription.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Invoice entity.
 *
 * Manages billing invoices including:
 * - Invoice generation
 * - Payment tracking
 * - Line items
 * - Reminders
 */
@Entity
@Table(name = "invoices", indexes = {
    @Index(name = "idx_invoices_tenant", columnList = "tenant_id"),
    @Index(name = "idx_invoices_subscription", columnList = "subscription_id"),
    @Index(name = "idx_invoices_customer", columnList = "customer_id"),
    @Index(name = "idx_invoices_status", columnList = "status"),
    @Index(name = "idx_invoices_due_date", columnList = "due_date"),
    @Index(name = "idx_invoices_number", columnList = "invoice_number")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Human-readable invoice number
     */
    @Column(name = "invoice_number", nullable = false, unique = true, length = 100)
    private String invoiceNumber;

    /**
     * Reference to subscription
     */
    @Column(name = "subscription_id", nullable = false)
    private String subscriptionId;

    /**
     * Customer ID
     */
    @Column(name = "customer_id", nullable = false, length = 255)
    private String customerId;

    /**
     * Invoice date
     */
    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    /**
     * Payment due date
     */
    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    /**
     * Billing period start
     */
    @Column(name = "period_start", nullable = false)
    private LocalDateTime periodStart;

    /**
     * Billing period end
     */
    @Column(name = "period_end", nullable = false)
    private LocalDateTime periodEnd;

    /**
     * Invoice amounts
     */
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    /**
     * Invoice status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private InvoiceStatus status;

    /**
     * Payment tracking
     */
    @Column(name = "paid_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * Payment method
     */
    @Column(name = "payment_method", length = 255)
    private String paymentMethod;

    /**
     * Payment date
     */
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    /**
     * Payment reference
     */
    @Column(name = "payment_reference", length = 255)
    private String paymentReference;

    /**
     * Reminders sent
     */
    @Column(name = "reminders_sent")
    @Builder.Default
    private Integer remindersSent = 0;

    /**
     * Last reminder sent
     */
    @Column(name = "last_reminder_sent")
    private LocalDateTime lastReminderSent;

    /**
     * PDF URL
     */
    @Lob
    @Column(name = "pdf_url")
    private String pdfUrl;

    /**
     * Line items (JSON array)
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonListMapConverter.class)
    @Column(name = "line_items")
    private List<Map<String, Object>> lineItems;

    /**
     * Additional metadata
     */
    @Convert(converter = com.gogidix.platform.subscription.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    /**
     * Stripe invoice ID
     */
    @Column(name = "stripe_invoice_id", length = 255)
    private String stripeInvoiceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (invoiceNumber == null) {
            invoiceNumber = generateInvoiceNumber();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Check if invoice is overdue
     */
    public boolean isOverdue() {
        return status != InvoiceStatus.PAID &&
               status != InvoiceStatus.VOID &&
               LocalDateTime.now().isAfter(dueDate);
    }

    /**
     * Check if invoice is partially paid
     */
    public boolean isPartiallyPaid() {
        return paidAmount.compareTo(BigDecimal.ZERO) > 0 &&
               paidAmount.compareTo(totalAmount) < 0;
    }

    /**
     * Get remaining amount due
     */
    public BigDecimal getRemainingAmount() {
        return totalAmount.subtract(paidAmount);
    }

    /**
     * Record payment
     */
    public void recordPayment(BigDecimal amount, String reference) {
        this.paidAmount = this.paidAmount.add(amount);
        this.paymentReference = reference;
        this.paymentDate = LocalDateTime.now();

        if (this.paidAmount.compareTo(totalAmount) >= 0) {
            this.status = InvoiceStatus.PAID;
        } else {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        }
    }

    /**
     * Mark as overdue
     */
    public void markOverdue() {
        if (status != InvoiceStatus.PAID) {
            this.status = InvoiceStatus.OVERDUE;
        }
    }

    /**
     * Void invoice
     */
    public void voidInvoice() {
        if (status == InvoiceStatus.PAID) {
            throw new IllegalStateException("Cannot void paid invoice");
        }
        this.status = InvoiceStatus.VOID;
    }

    /**
     * Generate invoice number
     */
    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public enum InvoiceStatus {
        DRAFT,
        PENDING,
        PAID,
        OVERDUE,
        VOID,
        PARTIALLY_PAID
    }
}

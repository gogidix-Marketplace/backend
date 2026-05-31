package com.gogidix.shared.infrastructure.services.billing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Invoice domain model.
 * Represents a billing invoice for a tenant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private String invoiceId;
    private String tenantId;
    private String subscriptionId;
    private String invoiceNumber;
    private InvoiceStatus status;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private String currency;
    private BillingPeriod billingPeriod;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private List<InvoiceLineItem> lineItems;
    private List<Payment> payments;
    private String notes;
    private String pdfUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Invoice status.
     */
    public enum InvoiceStatus {
        DRAFT,
        PENDING,
        PROCESSING,
        PAID,
        PARTIALLY_PAID,
        OVERDUE,
        VOID,
        CANCELLED
    }

    /**
     * Billing period type.
     */
    public enum BillingPeriod {
        MONTHLY,
        YEARLY,
        QUARTERLY,
        ONE_TIME,
        CUSTOM
    }

    /**
     * Add a line item to the invoice.
     */
    public void addLineItem(String description, BigDecimal quantity, BigDecimal unitPrice, String taxCode) {
        if (lineItems == null) {
            lineItems = new ArrayList<>();
        }

        InvoiceLineItem item = InvoiceLineItem.builder()
            .description(description)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .taxCode(taxCode)
            .build();

        lineItems.add(item);
        recalculateTotals();
    }

    /**
     * Recalculate invoice totals.
     */
    public void recalculateTotals() {
        BigDecimal subtotal = BigDecimal.ZERO;

        if (lineItems != null) {
            for (InvoiceLineItem item : lineItems) {
                subtotal = subtotal.add(item.getTotal());
            }
        }

        this.subtotal = subtotal;

        BigDecimal total = subtotal;
        if (taxAmount != null) {
            total = total.add(taxAmount);
        }
        if (discountAmount != null) {
            total = total.subtract(discountAmount);
        }

        this.totalAmount = total;
        this.balanceDue = total.subtract(amountPaid != null ? amountPaid : BigDecimal.ZERO);
    }

    /**
     * Check if invoice is overdue.
     */
    public boolean isOverdue() {
        return status == InvoiceStatus.OVERDUE ||
               (status != InvoiceStatus.PAID &&
                status != InvoiceStatus.VOID &&
                dueDate != null &&
                LocalDateTime.now().isAfter(dueDate));
    }

    /**
     * Get amount remaining to be paid.
     */
    public BigDecimal getRemainingAmount() {
        return balanceDue != null ? balanceDue : totalAmount;
    }

    /**
     * Mark invoice as paid.
     */
    public void markAsPaid(LocalDateTime paidDate) {
        this.status = InvoiceStatus.PAID;
        this.paidDate = paidDate;
        this.amountPaid = totalAmount;
        this.balanceDue = BigDecimal.ZERO;
    }

    /**
     * Generate a unique invoice number.
     */
    public static String generateInvoiceNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "INV-" + timestamp.substring(timestamp.length() - 10);
    }

    /**
     * Generate invoice ID.
     */
    public static String generateInvoiceId() {
        return "inv-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Inner class for line items.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItem {
        private String itemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal discount;
        private String taxCode;
        private BigDecimal taxRate;
        private BigDecimal total;

        public BigDecimal getTotal() {
            BigDecimal itemTotal = quantity.multiply(unitPrice);
            if (discount != null) {
                itemTotal = itemTotal.subtract(discount);
            }
            return itemTotal;
        }
    }
}

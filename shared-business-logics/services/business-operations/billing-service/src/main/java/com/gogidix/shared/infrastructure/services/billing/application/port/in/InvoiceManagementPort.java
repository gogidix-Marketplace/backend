package com.gogidix.shared.infrastructure.services.billing.application.port.in;

import com.gogidix.shared.infrastructure.services.billing.domain.model.Invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Input port for invoice management operations.
 * Defines the contract for invoice use cases.
 */
public interface InvoiceManagementPort {

    /**
     * Create a new invoice.
     */
    Invoice createInvoice(String tenantId,
                         String subscriptionId,
                         List<Invoice.InvoiceLineItem> lineItems,
                         LocalDateTime dueDate);

    /**
     * Get invoice by ID.
     */
    Optional<Invoice> getInvoice(String invoiceId);

    /**
     * Get all invoices for a tenant.
     */
    List<Invoice> getTenantInvoices(String tenantId);

    /**
     * Get invoices for a subscription.
     */
    List<Invoice> getSubscriptionInvoices(String subscriptionId);

    /**
     * Generate invoice for a subscription billing period.
     */
    Invoice generateSubscriptionInvoice(String subscriptionId);

    /**
     * Mark invoice as paid.
     */
    boolean markInvoiceAsPaid(String invoiceId, LocalDateTime paidDate);

    /**
     * Void an invoice.
     */
    boolean voidInvoice(String invoiceId, String reason);

    /**
     * Get overdue invoices.
     */
    List<Invoice> getOverdueInvoices(String tenantId);

    /**
     * Get pending invoices.
     */
    List<Invoice> getPendingInvoices(String tenantId);

    /**
     * Send invoice reminder.
     */
    boolean sendInvoiceReminder(String invoiceId);
}

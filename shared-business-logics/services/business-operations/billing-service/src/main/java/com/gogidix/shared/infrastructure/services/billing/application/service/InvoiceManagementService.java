package com.gogidix.shared.infrastructure.services.billing.application.service;

import com.gogidix.shared.infrastructure.services.billing.application.port.in.InvoiceManagementPort;
import com.gogidix.shared.infrastructure.services.billing.domain.aggregate.BillingRegistry;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Invoice;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Application service for invoice management.
 * Implements the InvoiceManagementPort using the BillingRegistry aggregate.
 */
@Service
public class InvoiceManagementService implements InvoiceManagementPort {

    private static final Logger log = LoggerFactory.getLogger(InvoiceManagementService.class);

    private final BillingRegistry billingRegistry;

    public InvoiceManagementService(BillingRegistry billingRegistry) {
        this.billingRegistry = billingRegistry;
    }

    @Override
    public Invoice createInvoice(String tenantId,
                                String subscriptionId,
                                List<Invoice.InvoiceLineItem> lineItems,
                                LocalDateTime dueDate) {
        log.info("Creating invoice: tenantId={}, subscriptionId={}", tenantId, subscriptionId);
        return billingRegistry.createInvoice(tenantId, subscriptionId, lineItems, dueDate);
    }

    @Override
    public Optional<Invoice> getInvoice(String invoiceId) {
        return billingRegistry.getInvoice(invoiceId);
    }

    @Override
    public List<Invoice> getTenantInvoices(String tenantId) {
        return billingRegistry.getTenantInvoices(tenantId);
    }

    @Override
    public List<Invoice> getSubscriptionInvoices(String subscriptionId) {
        log.info("Getting invoices for subscription: subscriptionId={}", subscriptionId);
        // Implementation would query repository
        return List.of();
    }

    @Override
    public Invoice generateSubscriptionInvoice(String subscriptionId) {
        log.info("Generating subscription invoice: subscriptionId={}", subscriptionId);

        Optional<Subscription> subscriptionOpt = billingRegistry.getSubscription(subscriptionId);
        if (subscriptionOpt.isEmpty()) {
            log.warn("Subscription not found for invoice generation: {}", subscriptionId);
            return null;
        }

        Subscription subscription = subscriptionOpt.get();

        List<Invoice.InvoiceLineItem> lineItems = new ArrayList<>();
        lineItems.add(Invoice.InvoiceLineItem.builder()
            .description("Subscription - " + subscription.getPlan() + " Plan (" +
                        subscription.getBillingPeriod() + ")")
            .quantity(java.math.BigDecimal.ONE)
            .unitPrice(subscription.getCurrentPrice())
            .build());

        LocalDateTime dueDate = subscription.getNextBillingDate() != null
            ? subscription.getNextBillingDate()
            : LocalDateTime.now().plusDays(30);

        return createInvoice(
            subscription.getTenantId(),
            subscriptionId,
            lineItems,
            dueDate
        );
    }

    @Override
    public boolean markInvoiceAsPaid(String invoiceId, LocalDateTime paidDate) {
        log.info("Marking invoice as paid: invoiceId={}", invoiceId);
        Optional<Invoice> invoiceOpt = billingRegistry.getInvoice(invoiceId);

        if (invoiceOpt.isEmpty()) {
            log.warn("Invoice not found: {}", invoiceId);
            return false;
        }

        Invoice invoice = invoiceOpt.get();
        invoice.markAsPaid(paidDate);

        return true;
    }

    @Override
    public boolean voidInvoice(String invoiceId, String reason) {
        log.info("Voiding invoice: invoiceId={}, reason={}", invoiceId, reason);
        Optional<Invoice> invoiceOpt = billingRegistry.getInvoice(invoiceId);

        if (invoiceOpt.isEmpty()) {
            log.warn("Invoice not found for voiding: {}", invoiceId);
            return false;
        }

        Invoice invoice = invoiceOpt.get();
        invoice.setStatus(Invoice.InvoiceStatus.VOID);
        invoice.setNotes(reason);
        invoice.setUpdatedAt(LocalDateTime.now());

        return true;
    }

    @Override
    public List<Invoice> getOverdueInvoices(String tenantId) {
        log.info("Getting overdue invoices: tenantId={}", tenantId);
        return billingRegistry.getTenantInvoices(tenantId).stream()
            .filter(Invoice::isOverdue)
            .toList();
    }

    @Override
    public List<Invoice> getPendingInvoices(String tenantId) {
        log.info("Getting pending invoices: tenantId={}", tenantId);
        return billingRegistry.getTenantInvoices(tenantId).stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.PENDING ||
                       i.getStatus() == Invoice.InvoiceStatus.PROCESSING)
            .toList();
    }

    @Override
    public boolean sendInvoiceReminder(String invoiceId) {
        log.info("Sending invoice reminder: invoiceId={}", invoiceId);
        // Implementation would integrate with notification service
        return true;
    }
}

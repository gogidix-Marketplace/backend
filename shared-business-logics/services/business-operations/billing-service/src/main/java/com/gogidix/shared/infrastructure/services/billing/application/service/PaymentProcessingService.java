package com.gogidix.shared.infrastructure.services.billing.application.service;

import com.gogidix.shared.infrastructure.services.billing.application.port.in.PaymentProcessingPort;
import com.gogidix.shared.infrastructure.services.billing.domain.aggregate.BillingRegistry;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Invoice;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Application service for payment processing.
 * Implements the PaymentProcessingPort using the BillingRegistry aggregate.
 */
@Service
public class PaymentProcessingService implements PaymentProcessingPort {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessingService.class);

    private final BillingRegistry billingRegistry;

    public PaymentProcessingService(BillingRegistry billingRegistry) {
        this.billingRegistry = billingRegistry;
    }

    @Override
    public Payment processPayment(String invoiceId,
                                 BigDecimal amount,
                                 Payment.PaymentMethod method,
                                 String paymentMethodId,
                                 String gateway,
                                 String cardLastFour,
                                 String cardBrand) {
        log.info("Processing payment: invoiceId={}, amount={}, method={}", invoiceId, amount, method);
        return billingRegistry.processPayment(
            invoiceId, amount, method, paymentMethodId, gateway, cardLastFour, cardBrand
        );
    }

    @Override
    public Optional<Payment> getPayment(String paymentId) {
        log.info("Getting payment: paymentId={}", paymentId);
        // Implementation would query repository
        return Optional.empty();
    }

    @Override
    public List<Payment> getInvoicePayments(String invoiceId) {
        log.info("Getting payments for invoice: invoiceId={}", invoiceId);
        return billingRegistry.getInvoicePayments(invoiceId);
    }

    @Override
    public List<Payment> getTenantPayments(String tenantId) {
        log.info("Getting payments for tenant: tenantId={}", tenantId);
        // Implementation would query repository
        return new ArrayList<>();
    }

    @Override
    public boolean refundPayment(String paymentId, BigDecimal amount, String reason) {
        log.info("Refunding payment: paymentId={}, amount={}, reason={}", paymentId, amount, reason);
        // Implementation would:
        // 1. Get payment from repository
        // 2. Call gateway refund API
        // 3. Update payment status to REFUNDED or PARTIALLY_REFUNDED
        // 4. Update invoice amount_paid
        return true;
    }

    @Override
    public boolean partiallyRefundPayment(String paymentId, BigDecimal amount, String reason) {
        log.info("Partially refunding payment: paymentId={}, amount={}, reason={}", paymentId, amount, reason);
        // Similar to refundPayment but sets PARTIALLY_REFUNDED status
        return true;
    }

    @Override
    public boolean processGatewayWebhook(String gateway, String payload, String signature) {
        log.info("Processing gateway webhook: gateway={}", gateway);
        // Implementation would:
        // 1. Verify signature
        // 2. Parse webhook payload
        // 3. Update payment/invoice status based on event type
        return true;
    }

    @Override
    public String savePaymentMethod(String tenantId, String paymentMethodToken, String gateway) {
        log.info("Saving payment method: tenantId={}, gateway={}", tenantId, gateway);
        // Implementation would:
        // 1. Validate token with gateway
        // 2. Save to database encrypted
        // 3. Return payment method ID
        return "pm-" + System.currentTimeMillis();
    }

    @Override
    public List<String> getTenantPaymentMethods(String tenantId) {
        log.info("Getting payment methods for tenant: tenantId={}", tenantId);
        // Implementation would query repository
        return new ArrayList<>();
    }

    @Override
    public boolean removePaymentMethod(String paymentMethodId) {
        log.info("Removing payment method: paymentMethodId={}", paymentMethodId);
        // Implementation would:
        // 1. Check if method is used by active subscription
        // 2. Delete from repository
        // 3. Optionally call gateway to remove
        return true;
    }
}

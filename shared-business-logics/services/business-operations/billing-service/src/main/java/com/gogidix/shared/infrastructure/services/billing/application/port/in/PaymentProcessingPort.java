package com.gogidix.shared.infrastructure.services.billing.application.port.in;

import com.gogidix.shared.infrastructure.services.billing.domain.model.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Input port for payment processing operations.
 * Defines the contract for payment use cases.
 */
public interface PaymentProcessingPort {

    /**
     * Process a payment for an invoice.
     */
    Payment processPayment(String invoiceId,
                          BigDecimal amount,
                          Payment.PaymentMethod method,
                          String paymentMethodId,
                          String gateway,
                          String cardLastFour,
                          String cardBrand);

    /**
     * Get payment by ID.
     */
    Optional<Payment> getPayment(String paymentId);

    /**
     * Get all payments for an invoice.
     */
    List<Payment> getInvoicePayments(String invoiceId);

    /**
     * Get all payments for a tenant.
     */
    List<Payment> getTenantPayments(String tenantId);

    /**
     * Refund a payment.
     */
    boolean refundPayment(String paymentId, BigDecimal amount, String reason);

    /**
     * Partially refund a payment.
     */
    boolean partiallyRefundPayment(String paymentId, BigDecimal amount, String reason);

    /**
     * Process webhook from payment gateway.
     */
    boolean processGatewayWebhook(String gateway, String payload, String signature);

    /**
     * Save payment method for future use.
     */
    String savePaymentMethod(String tenantId, String paymentMethodToken, String gateway);

    /**
     * Get saved payment methods for a tenant.
     */
    List<String> getTenantPaymentMethods(String tenantId);

    /**
     * Remove a saved payment method.
     */
    boolean removePaymentMethod(String paymentMethodId);
}

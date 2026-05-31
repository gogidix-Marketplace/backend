package com.gogidix.shared.infrastructure.services.businessoperations.payment.domain.model;

import com.gogidix.shared.servicediscovery.config.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Payment domain model.
 */
@DisplayName("Payment Domain Model Tests")
class PaymentTest {

    @Test
    @DisplayName("Should create payment with default constructor")
    void shouldCreatePaymentWithDefaultConstructor() {
        Payment payment = new Payment();

        assertNull(payment.getId());
        assertNull(payment.getTenantId());
        assertNull(payment.getPaymentId());
        assertNull(payment.getCustomerId());
        assertNull(payment.getOrderId());
        assertNull(payment.getAmount());
        assertNull(payment.getCurrency());
        assertNull(payment.getStatus());
        assertNull(payment.getPaymentMethod());
        assertNull(payment.getPaymentMethodDetails());
        assertNull(payment.getGatewayTransactionId());
        assertNull(payment.getGateway());
        assertNull(payment.getFailureReason());
        assertNull(payment.getProcessedAt());
        assertNull(payment.getRefundedAt());
        assertNull(payment.getRefundId());
        assertNull(payment.getRefundAmount());
        assertNull(payment.getCreatedAt());
        assertNull(payment.getUpdatedAt());
        assertNull(payment.getCustomerEmail());
        assertNull(payment.getCustomerPhone());
        assertNull(payment.getBillingAddress());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        Payment payment = new Payment();
        Payment.BillingAddress address = new Payment.BillingAddress();
        address.setLine1("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setPostalCode("10001");
        address.setCountry("USA");

        payment.setId("payment-123");
        payment.setTenantId(TenantId.of("tenant-001"));
        payment.setPaymentId("pay-123");
        payment.setCustomerId("customer-123");
        payment.setOrderId("order-123");
        payment.setAmount(new BigDecimal("100.50"));
        payment.setCurrency("USD");
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
        payment.setPaymentMethodDetails("Visa ending in 4242");
        payment.setGatewayTransactionId("txn-123");
        payment.setGateway("stripe");
        payment.setFailureReason(null);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setCustomerEmail("customer@example.com");
        payment.setCustomerPhone("+1234567890");
        payment.setBillingAddress(address);

        assertEquals("payment-123", payment.getId());
        assertEquals("tenant-001", payment.getTenantId().getValue());
        assertEquals("pay-123", payment.getPaymentId());
        assertEquals("customer-123", payment.getCustomerId());
        assertEquals("order-123", payment.getOrderId());
        assertEquals(new BigDecimal("100.50"), payment.getAmount());
        assertEquals("USD", payment.getCurrency());
        assertEquals(Payment.PaymentStatus.PENDING, payment.getStatus());
        assertEquals(Payment.PaymentMethod.CREDIT_CARD, payment.getPaymentMethod());
        assertEquals("Visa ending in 4242", payment.getPaymentMethodDetails());
        assertEquals("txn-123", payment.getGatewayTransactionId());
        assertEquals("stripe", payment.getGateway());
        assertEquals("customer@example.com", payment.getCustomerEmail());
        assertEquals("+1234567890", payment.getCustomerPhone());
        assertNotNull(payment.getBillingAddress());
        assertEquals("New York", payment.getBillingAddress().getCity());
    }

    @Test
    @DisplayName("Should mark payment as completed")
    void shouldMarkPaymentAsCompleted() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.PROCESSING);

        payment.markAsCompleted("txn-456");

        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals("txn-456", payment.getGatewayTransactionId());
        assertNotNull(payment.getProcessedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark payment as failed")
    void shouldMarkPaymentAsFailed() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.PROCESSING);

        payment.markAsFailed("Insufficient funds");

        assertEquals(Payment.PaymentStatus.FAILED, payment.getStatus());
        assertEquals("Insufficient funds", payment.getFailureReason());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark payment as fully refunded")
    void shouldMarkPaymentAsFullyRefunded() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setAmount(new BigDecimal("100.00"));

        payment.markAsRefunded("refund-123", new BigDecimal("100.00"));

        assertEquals(Payment.PaymentStatus.REFUNDED, payment.getStatus());
        assertEquals("refund-123", payment.getRefundId());
        assertEquals(new BigDecimal("100.00"), payment.getRefundAmount());
        assertNotNull(payment.getRefundedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should mark payment as partially refunded")
    void shouldMarkPaymentAsPartiallyRefunded() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setAmount(new BigDecimal("100.00"));

        payment.markAsRefunded("refund-456", new BigDecimal("50.00"));

        assertEquals(Payment.PaymentStatus.PARTIALLY_REFUNDED, payment.getStatus());
        assertEquals("refund-456", payment.getRefundId());
        assertEquals(new BigDecimal("50.00"), payment.getRefundAmount());
        assertNotNull(payment.getRefundedAt());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception when refunding already refunded payment")
    void shouldThrowExceptionWhenRefundingAlreadyRefundedPayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setAmount(new BigDecimal("100.00"));

        assertThrows(IllegalStateException.class, () ->
                payment.markAsRefunded("refund-789", new BigDecimal("50.00"))
        );
    }

    @Test
    @DisplayName("Should throw exception when refund amount exceeds payment amount")
    void shouldThrowExceptionWhenRefundAmountExceedsPaymentAmount() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setAmount(new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () ->
                payment.markAsRefunded("refund-999", new BigDecimal("150.00"))
        );
    }

    @Test
    @DisplayName("Should cancel pending payment")
    void shouldCancelPendingPayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.PENDING);

        payment.cancel();

        assertEquals(Payment.PaymentStatus.CANCELLED, payment.getStatus());
        assertNotNull(payment.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-pending payment")
    void shouldThrowExceptionWhenCancellingNonPendingPayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    @DisplayName("Should return true for refundable payment")
    void shouldReturnTrueForRefundablePayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        assertTrue(payment.isRefundable());
    }

    @Test
    @DisplayName("Should return false for non-refundable payment statuses")
    void shouldReturnFalseForNonRefundablePaymentStatuses() {
        Payment pendingPayment = new Payment();
        pendingPayment.setStatus(Payment.PaymentStatus.PENDING);

        Payment failedPayment = new Payment();
        failedPayment.setStatus(Payment.PaymentStatus.FAILED);

        Payment cancelledPayment = new Payment();
        cancelledPayment.setStatus(Payment.PaymentStatus.CANCELLED);

        Payment refundedPayment = new Payment();
        refundedPayment.setStatus(Payment.PaymentStatus.REFUNDED);

        assertFalse(pendingPayment.isRefundable());
        assertFalse(failedPayment.isRefundable());
        assertFalse(cancelledPayment.isRefundable());
        assertFalse(refundedPayment.isRefundable());
    }

    @Test
    @DisplayName("Should handle all PaymentStatus enum values")
    void shouldHandleAllPaymentStatusEnums() {
        assertEquals(7, Payment.PaymentStatus.values().length);
        assertEquals(Payment.PaymentStatus.PENDING, Payment.PaymentStatus.valueOf("PENDING"));
        assertEquals(Payment.PaymentStatus.PROCESSING, Payment.PaymentStatus.valueOf("PROCESSING"));
        assertEquals(Payment.PaymentStatus.COMPLETED, Payment.PaymentStatus.valueOf("COMPLETED"));
        assertEquals(Payment.PaymentStatus.FAILED, Payment.PaymentStatus.valueOf("FAILED"));
        assertEquals(Payment.PaymentStatus.CANCELLED, Payment.PaymentStatus.valueOf("CANCELLED"));
        assertEquals(Payment.PaymentStatus.REFUNDED, Payment.PaymentStatus.valueOf("REFUNDED"));
        assertEquals(Payment.PaymentStatus.PARTIALLY_REFUNDED, Payment.PaymentStatus.valueOf("PARTIALLY_REFUNDED"));
    }

    @Test
    @DisplayName("Should handle all PaymentMethod enum values")
    void shouldHandleAllPaymentMethodEnums() {
        assertEquals(8, Payment.PaymentMethod.values().length);
        assertEquals(Payment.PaymentMethod.CREDIT_CARD, Payment.PaymentMethod.valueOf("CREDIT_CARD"));
        assertEquals(Payment.PaymentMethod.DEBIT_CARD, Payment.PaymentMethod.valueOf("DEBIT_CARD"));
        assertEquals(Payment.PaymentMethod.BANK_TRANSFER, Payment.PaymentMethod.valueOf("BANK_TRANSFER"));
        assertEquals(Payment.PaymentMethod.MOBILE_MONEY, Payment.PaymentMethod.valueOf("MOBILE_MONEY"));
        assertEquals(Payment.PaymentMethod.CRYPTO, Payment.PaymentMethod.valueOf("CRYPTO"));
        assertEquals(Payment.PaymentMethod.WALLET, Payment.PaymentMethod.valueOf("WALLET"));
        assertEquals(Payment.PaymentMethod.PAYPAL, Payment.PaymentMethod.valueOf("PAYPAL"));
        assertEquals(Payment.PaymentMethod.STRIPE, Payment.PaymentMethod.valueOf("STRIPE"));
    }

    @Test
    @DisplayName("Should set and get billing address properties")
    void shouldSetAndGetBillingAddressProperties() {
        Payment.BillingAddress address = new Payment.BillingAddress();

        address.setLine1("456 Oak Ave");
        address.setLine2("Apt 2B");
        address.setCity("Los Angeles");
        address.setState("CA");
        address.setPostalCode("90001");
        address.setCountry("USA");
        address.setCountryCode("US");

        assertEquals("456 Oak Ave", address.getLine1());
        assertEquals("Apt 2B", address.getLine2());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals("90001", address.getPostalCode());
        assertEquals("USA", address.getCountry());
        assertEquals("US", address.getCountryCode());
    }

    @Test
    @DisplayName("Should handle billing address with null values")
    void shouldHandleBillingAddressWithNullValues() {
        Payment.BillingAddress address = new Payment.BillingAddress();

        assertNull(address.getLine1());
        assertNull(address.getLine2());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getPostalCode());
        assertNull(address.getCountry());
        assertNull(address.getCountryCode());
    }

    @Test
    @DisplayName("Should process payment through lifecycle")
    void shouldProcessPaymentThroughLifecycle() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setAmount(new BigDecimal("100.00"));

        // Process
        payment.setStatus(Payment.PaymentStatus.PROCESSING);

        // Complete
        payment.markAsCompleted("txn-123");
        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());

        // Partial refund
        payment.markAsRefunded("refund-123", new BigDecimal("30.00"));
        assertEquals(Payment.PaymentStatus.PARTIALLY_REFUNDED, payment.getStatus());
        assertEquals(new BigDecimal("30.00"), payment.getRefundAmount());
    }

    @Test
    @DisplayName("Should handle different payment methods")
    void shouldHandleDifferentPaymentMethods() {
        Payment creditCardPayment = new Payment();
        creditCardPayment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);

        Payment paypalPayment = new Payment();
        paypalPayment.setPaymentMethod(Payment.PaymentMethod.PAYPAL);

        Payment cryptoPayment = new Payment();
        cryptoPayment.setPaymentMethod(Payment.PaymentMethod.CRYPTO);

        assertEquals(Payment.PaymentMethod.CREDIT_CARD, creditCardPayment.getPaymentMethod());
        assertEquals(Payment.PaymentMethod.PAYPAL, paypalPayment.getPaymentMethod());
        assertEquals(Payment.PaymentMethod.CRYPTO, cryptoPayment.getPaymentMethod());
    }

    @Test
    @DisplayName("Should handle zero amount payment")
    void shouldHandleZeroAmountPayment() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.ZERO);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        // Zero amount should be refundable
        assertTrue(payment.isRefundable());

        // Refunding zero amount should work
        payment.markAsRefunded("refund-zero", BigDecimal.ZERO);
        assertEquals(Payment.PaymentStatus.REFUNDED, payment.getStatus());
    }

    @Test
    @DisplayName("Should handle very small refund amount")
    void shouldHandleVerySmallRefundAmount() {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("100.00"));
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        payment.markAsRefunded("refund-small", new BigDecimal("0.01"));

        assertEquals(Payment.PaymentStatus.PARTIALLY_REFUNDED, payment.getStatus());
        assertEquals(new BigDecimal("0.01"), payment.getRefundAmount());
    }
}

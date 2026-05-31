package com.gogidix.shared.infrastructure.services.businessoperations.payment.domain.model;

import com.gogidix.shared.multitenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity representing a payment transaction.
 */
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private TenantId tenantId;

    @Indexed
    private String paymentId;

    @Indexed
    private String customerId;

    @Indexed
    private String orderId;

    private BigDecimal amount;

    @Indexed
    private String currency;

    @Indexed
    private PaymentStatus status;

    @Indexed
    private PaymentMethod paymentMethod;

    private String paymentMethodDetails;
    private String gatewayTransactionId;
    private String gateway;
    private String failureReason;

    private LocalDateTime processedAt;
    private LocalDateTime refundedAt;

    private String refundId;
    private BigDecimal refundAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerEmail;
    private String customerPhone;

    private BillingAddress billingAddress;

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REFUNDED,
        PARTIALLY_REFUNDED
    }

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        BANK_TRANSFER,
        MOBILE_MONEY,
        CRYPTO,
        WALLET,
        PAYPAL,
        STRIPE
    }

    public static class BillingAddress {
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String countryCode;

        // Getters and Setters
        public String getLine1() { return line1; }
        public void setLine1(String line1) { this.line1 = line1; }
        public String getLine2() { return line2; }
        public void setLine2(String line2) { this.line2 = line2; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    }

    public Payment() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentMethodDetails() { return paymentMethodDetails; }
    public void setPaymentMethodDetails(String paymentMethodDetails) { this.paymentMethodDetails = paymentMethodDetails; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }

    public String getGateway() { return gateway; }
    public void setGateway(String gateway) { this.gateway = gateway; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getRefundedAt() { return refundedAt; }
    public void setRefundedAt(LocalDateTime refundedAt) { this.refundedAt = refundedAt; }

    public String getRefundId() { return refundId; }
    public void setRefundId(String refundId) { this.refundId = refundId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public BillingAddress getBillingAddress() { return billingAddress; }
    public void setBillingAddress(BillingAddress billingAddress) { this.billingAddress = billingAddress; }

    // Domain methods
    public void markAsCompleted(String transactionId) {
        this.status = PaymentStatus.COMPLETED;
        this.gatewayTransactionId = transactionId;
        this.processedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRefunded(String refundId, BigDecimal refundAmount) {
        if (this.status == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Payment already refunded");
        }
        if (refundAmount.compareTo(this.amount) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed payment amount");
        }
        this.refundId = refundId;
        this.refundAmount = refundAmount;
        this.refundedAt = LocalDateTime.now();
        this.status = refundAmount.compareTo(this.amount) == 0
                ? PaymentStatus.REFUNDED
                : PaymentStatus.PARTIALLY_REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be cancelled");
        }
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isRefundable() {
        return this.status == PaymentStatus.COMPLETED;
    }
}

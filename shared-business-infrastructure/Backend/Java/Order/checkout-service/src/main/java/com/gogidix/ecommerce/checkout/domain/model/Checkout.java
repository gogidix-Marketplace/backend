package com.gogidix.ecommerce.checkout.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "checkouts")
public class Checkout {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    private String checkoutId;

    @Indexed
    private String customerId;
    private String cartId;

    private CheckoutStatus status;
    private String orderId;

    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;

    private String shippingMethod;
    private BigDecimal shippingAmount;

    private String paymentMethod;
    private PaymentInfo paymentInfo;

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;

    private String currency;
    private String promoCode;

    private List<CheckoutItem> items;

    private String notes;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;

    public enum CheckoutStatus {
        INITIATED, SHIPPING_INFO, PAYMENT_PENDING, PAYMENT_PROCESSING,
        COMPLETED, FAILED, CANCELLED, EXPIRED
    }

    @Data
    public static class ShippingAddress {
        private String firstName;
        private String lastName;
        private String company;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String phone;
    }

    @Data
    public static class BillingAddress {
        private String firstName;
        private String lastName;
        private String company;
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String postalCode;
        private String country;
    }

    @Data
    public static class PaymentInfo {
        private String cardLast4;
        private String cardType;
        private String transactionId;
    }

    @Data
    public static class CheckoutItem {
        private String productId;
        private String sku;
        private String name;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal lineTotal;
    }
}

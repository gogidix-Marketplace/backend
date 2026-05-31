package com.gogidix.ecommerce.cart.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String cartId;

    @Indexed
    private String customerId;

    private CartType cartType;
    private CartStatus status;

    private List<CartItem> items = new ArrayList<>();

    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;

    private String currency;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant expiresAt;

    private String createdBy;
    private String updatedBy;

    public void markAsUpdated() {
        this.updatedAt = Instant.now();
    }

    public enum CartType {
        GUEST, CUSTOMER, CORPORATE
    }

    public enum CartStatus {
        ACTIVE, ABANDONED, CONVERTED, EXPIRED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String itemId;
        private String productId;
        private String sku;
        private String name;
        private String imageUrl;

        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal lineTotal;

        private String vendorId;
        private Boolean inStock;
    }
}

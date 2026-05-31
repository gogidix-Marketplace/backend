package com.gogidix.ecommerce.cart.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class CartItem {

    private String sku;
    private String productName;
    private String productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String currency;
    private Map<String, String> attributes;
    private Instant addedAt;
    private Instant updatedAt;

    public static CartItem create(String sku, String productId, String productName,
                                   int quantity, BigDecimal unitPrice, String currency) {
        CartItem item = new CartItem();
        item.sku = sku;
        item.productId = productId;
        item.productName = productName;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        item.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
        item.currency = currency;
        item.addedAt = Instant.now();
        item.updatedAt = Instant.now();
        return item;
    }

    // Getters and Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

package com.gogidix.ecommerce.cart.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
public class Cart extends BaseEntity {

    @Indexed
    @Field("cart_id")
    private String cartId;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Field("status")
    private CartStatus status;

    private String currency;
    private List<CartItem> items = new ArrayList<>();
    private CartSummary summary;
    @Field("channel_id")
    private String channelId;
    @Field("expires_at")
    private Instant expiresAt;
    private Integer version;

    public enum CartStatus {
        ACTIVE, LOCKED, ABANDONED, CONVERTED, EXPIRED
    }

    public Cart() {
        super();
        this.status = CartStatus.ACTIVE;
        this.expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days
        this.version = 0;
        this.summary = new CartSummary();
    }

    public Cart(String tenantId) {
        super(tenantId);
        this.cartId = java.util.UUID.randomUUID().toString();
        this.status = CartStatus.ACTIVE;
        this.expiresAt = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days
        this.version = 0;
        this.summary = new CartSummary();
    }

    public static Cart create(String tenantId, String customerId, String currency, String channelId) {
        Cart cart = new Cart(tenantId);
        cart.customerId = customerId;
        cart.currency = currency;
        cart.channelId = channelId;
        return cart;
    }

    public void addItem(CartItem item) {
        items.removeIf(i -> i.getSku().equals(item.getSku()));
        items.add(item);
        recalculateSummary();
        updateTimestamp();
        version++;
    }

    public void removeItem(String sku) {
        items.removeIf(i -> i.getSku().equals(sku));
        recalculateSummary();
        updateTimestamp();
        version++;
    }

    public void updateItemQuantity(String sku, int quantity) {
        items.stream()
            .filter(i -> i.getSku().equals(sku))
            .findFirst()
            .ifPresent(i -> {
                i.setQuantity(quantity);
                i.setUpdatedAt(Instant.now());
            });
        recalculateSummary();
        updateTimestamp();
        version++;
    }

    public void clear() {
        items.clear();
        recalculateSummary();
        updateTimestamp();
        version++;
    }

    public void lock() {
        this.status = CartStatus.LOCKED;
        updateTimestamp();
        version++;
    }

    public void convert() {
        this.status = CartStatus.CONVERTED;
        updateTimestamp();
        version++;
    }

    private void recalculateSummary() {
        CartSummary newSummary = new CartSummary();
        newSummary.setItemCount(items.size());
        newSummary.setTotalQuantity(items.stream().mapToInt(CartItem::getQuantity).sum());
        newSummary.setSubtotal(items.stream()
            .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        this.summary = newSummary;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public CartStatus getStatus() { return status; }
    public void setStatus(CartStatus status) { this.status = status; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    public CartSummary getSummary() { return summary; }
    public void setSummary(CartSummary summary) { this.summary = summary; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}

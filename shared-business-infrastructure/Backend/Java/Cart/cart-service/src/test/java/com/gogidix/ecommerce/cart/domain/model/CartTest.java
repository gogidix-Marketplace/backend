package com.gogidix.ecommerce.cart.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Cart Domain Model Tests")
class CartTest {

    @Test
    @DisplayName("Should create cart with tenant ID")
    void shouldCreateCartWithTenantId() {
        String tenantId = "tenant-123";
        Cart cart = new Cart(tenantId);

        assertThat(cart.getTenantId()).isEqualTo(tenantId);
        assertThat(cart.getCartId()).isNotNull();
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);
        assertThat(cart.getVersion()).isEqualTo(0);
        assertThat(cart.getCreatedAt()).isNotNull();
        assertThat(cart.getUpdatedAt()).isNotNull();
        assertThat(cart.getExpiresAt()).isNotNull();
        assertThat(cart.getExpiresAt()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("Should create cart with factory method")
    void shouldCreateCartWithFactoryMethod() {
        String tenantId = "tenant-123";
        String customerId = "customer-456";
        String currency = "USD";
        String channelId = "web";

        Cart cart = Cart.create(tenantId, customerId, currency, channelId);

        assertThat(cart.getTenantId()).isEqualTo(tenantId);
        assertThat(cart.getCustomerId()).isEqualTo(customerId);
        assertThat(cart.getCurrency()).isEqualTo(currency);
        assertThat(cart.getChannelId()).isEqualTo(channelId);
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should add item to cart")
    void shouldAddItemToCart() {
        Cart cart = new Cart("tenant-123");
        CartItem item = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");

        cart.addItem(item);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getSku()).isEqualTo("SKU-001");
        assertThat(cart.getSummary().getItemCount()).isEqualTo(1);
        assertThat(cart.getSummary().getTotalQuantity()).isEqualTo(2);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("20.00");
        assertThat(cart.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should replace existing item when adding same SKU")
    void shouldReplaceExistingItemWhenAddingSameSku() {
        Cart cart = new Cart("tenant-123");
        CartItem item1 = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");
        CartItem item2 = CartItem.create("SKU-001", "prod-1", "Product 1",
                5, new BigDecimal("15.00"), "USD");

        cart.addItem(item1);
        cart.addItem(item2);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
        assertThat(cart.getSummary().getTotalQuantity()).isEqualTo(5);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("75.00");
    }

    @Test
    @DisplayName("Should remove item from cart")
    void shouldRemoveItemFromCart() {
        Cart cart = new Cart("tenant-123");
        CartItem item1 = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");
        CartItem item2 = CartItem.create("SKU-002", "prod-2", "Product 2",
                1, new BigDecimal("20.00"), "USD");

        cart.addItem(item1);
        cart.addItem(item2);
        cart.removeItem("SKU-001");

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getSku()).isEqualTo("SKU-002");
        assertThat(cart.getSummary().getItemCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should update item quantity")
    void shouldUpdateItemQuantity() {
        Cart cart = new Cart("tenant-123");
        CartItem item = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");

        cart.addItem(item);
        cart.updateItemQuantity("SKU-001", 5);

        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
        assertThat(cart.getItems().get(0).getTotalPrice()).isEqualByComparingTo("50.00");
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("50.00");
        assertThat(cart.getVersion()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should clear cart")
    void shouldClearCart() {
        Cart cart = new Cart("tenant-123");
        CartItem item1 = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");
        CartItem item2 = CartItem.create("SKU-002", "prod-2", "Product 2",
                1, new BigDecimal("20.00"), "USD");

        cart.addItem(item1);
        cart.addItem(item2);
        cart.clear();

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getSummary().getItemCount()).isEqualTo(0);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Should lock cart")
    void shouldLockCart() {
        Cart cart = new Cart("tenant-123");
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);

        cart.lock();

        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.LOCKED);
        assertThat(cart.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should convert cart")
    void shouldConvertCart() {
        Cart cart = new Cart("tenant-123");
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);

        cart.convert();

        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.CONVERTED);
        assertThat(cart.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should check if cart is expired")
    void shouldCheckIfCartIsExpired() {
        Cart cart = new Cart("tenant-123");
        cart.setExpiresAt(Instant.now().minusSeconds(60)); // expired 1 minute ago

        assertThat(cart.isExpired()).isTrue();
    }

    @Test
    @DisplayName("Should check if cart is not expired")
    void shouldCheckIfCartIsNotExpired() {
        Cart cart = new Cart("tenant-123");

        assertThat(cart.isExpired()).isFalse();
    }

    @Test
    @DisplayName("Should initialize with default summary")
    void shouldInitializeWithDefaultSummary() {
        Cart cart = new Cart("tenant-123");

        assertThat(cart.getSummary()).isNotNull();
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("0");
        assertThat(cart.getSummary().getItemCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should recalculate summary correctly")
    void shouldRecalculateSummaryCorrectly() {
        Cart cart = new Cart("tenant-123");
        CartItem item1 = CartItem.create("SKU-001", "prod-1", "Product 1",
                2, new BigDecimal("10.00"), "USD");
        CartItem item2 = CartItem.create("SKU-002", "prod-2", "Product 2",
                3, new BigDecimal("20.00"), "USD");

        cart.addItem(item1);
        cart.addItem(item2);

        assertThat(cart.getSummary().getItemCount()).isEqualTo(2);
        assertThat(cart.getSummary().getTotalQuantity()).isEqualTo(5);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("80.00");
    }

    @Test
    @DisplayName("Should update timestamp on operations")
    void shouldUpdateTimestampOnOperations() {
        Cart cart = new Cart("tenant-123");
        Instant beforeUpdate = cart.getUpdatedAt();

        cart.lock();
        Instant afterUpdate = cart.getUpdatedAt();

        assertThat(afterUpdate).isAfterOrEqualTo(beforeUpdate);
    }
}

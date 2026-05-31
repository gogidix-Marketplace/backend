package com.gogidix.ecommerce.cart.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class CartDomainTest {

    private CartItem createItem(String sku, int qty, BigDecimal price) {
        return CartItem.create(sku, "prod-" + sku, "Product " + sku, qty, price, "USD");
    }

    @Test void cart_create() {
        Cart cart = Cart.create("t1", "c1", "USD", "web");
        assertThat(cart.getCustomerId()).isEqualTo("c1");
        assertThat(cart.getCurrency()).isEqualTo("USD");
        assertThat(cart.getChannelId()).isEqualTo("web");
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);
        assertThat(cart.getVersion()).isEqualTo(0);
        assertThat(cart.getSummary()).isNotNull();
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getCartId()).isNotNull();
    }

    @Test void cart_addItem() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.addItem(createItem("sku1", 2, BigDecimal.TEN));
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getVersion()).isEqualTo(1);
        assertThat(cart.getSummary().getItemCount()).isEqualTo(1);
        assertThat(cart.getSummary().getTotalQuantity()).isEqualTo(2);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("20");
    }

    @Test void cart_addSameSkuReplaces() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.addItem(createItem("sku1", 2, BigDecimal.TEN));
        cart.addItem(createItem("sku1", 3, BigDecimal.TEN));
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getSummary().getTotalQuantity()).isEqualTo(3);
    }

    @Test void cart_removeItem() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.addItem(createItem("sku1", 1, BigDecimal.TEN));
        cart.removeItem("sku1");
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getSummary().getItemCount()).isEqualTo(0);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test void cart_updateItemQuantity() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.addItem(createItem("sku1", 2, BigDecimal.valueOf(5)));
        cart.updateItemQuantity("sku1", 5);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(5);
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo("25");
    }

    @Test void cart_clear() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.addItem(createItem("sku1", 1, BigDecimal.TEN));
        cart.clear();
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getSummary().getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test void cart_lock() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.lock();
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.LOCKED);
    }

    @Test void cart_convert() {
        Cart cart = Cart.create("t1", "c1", "USD", null);
        cart.convert();
        assertThat(cart.getStatus()).isEqualTo(Cart.CartStatus.CONVERTED);
    }

    @Test void cartStatus_enum() {
        assertThat(Cart.CartStatus.values()).hasSize(5);
        assertThat(Cart.CartStatus.valueOf("ABANDONED")).isEqualTo(Cart.CartStatus.ABANDONED);
    }

    @Test void cartItem_create() {
        CartItem item = CartItem.create("sku1", "p1", "Widget", 3, BigDecimal.valueOf(9.99), "USD");
        assertThat(item.getSku()).isEqualTo("sku1");
        assertThat(item.getProductId()).isEqualTo("p1");
        assertThat(item.getProductName()).isEqualTo("Widget");
        assertThat(item.getQuantity()).isEqualTo(3);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("9.99");
        assertThat(item.getTotalPrice()).isEqualByComparingTo("29.97");
        assertThat(item.getCurrency()).isEqualTo("USD");
        assertThat(item.getAddedAt()).isNotNull();
        assertThat(item.getUpdatedAt()).isNotNull();
    }

    @Test void cartItem_setQuantity_updatesTotal() {
        CartItem item = CartItem.create("sku1", "p1", "W", 2, BigDecimal.TEN, "USD");
        item.setQuantity(5);
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(item.getTotalPrice()).isEqualByComparingTo("50");
    }

    @Test void cartItem_attributes() {
        CartItem item = new CartItem();
        item.setAttributes(Map.of("color", "red"));
        assertThat(item.getAttributes()).containsEntry("color", "red");
    }

    @Test void cartSummary_defaults() {
        CartSummary s = new CartSummary();
        assertThat(s.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(s.getDiscountAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(s.getTaxAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(s.getShippingAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(s.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test void cartSummary_allFields() {
        CartSummary s = new CartSummary();
        s.setItemCount(3); s.setTotalQuantity(10);
        s.setSubtotal(BigDecimal.valueOf(100));
        s.setDiscountAmount(BigDecimal.TEN);
        s.setTaxAmount(BigDecimal.valueOf(5));
        s.setShippingAmount(BigDecimal.valueOf(15));
        s.setTotalAmount(BigDecimal.valueOf(110));
        assertThat(s.getItemCount()).isEqualTo(3);
        assertThat(s.getTotalQuantity()).isEqualTo(10);
        assertThat(s.getTotalAmount()).isEqualByComparingTo("110");
    }

    @Test void cartSummary_nullHandling() {
        CartSummary s = new CartSummary();
        s.setSubtotal(null); s.setDiscountAmount(null);
        assertThat(s.getSubtotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(s.getDiscountAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
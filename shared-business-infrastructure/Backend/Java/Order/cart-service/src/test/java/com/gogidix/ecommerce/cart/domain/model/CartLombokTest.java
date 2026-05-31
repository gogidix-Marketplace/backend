package com.gogidix.ecommerce.cart.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class CartLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private Cart createFull() {
        Cart c = new Cart();
        c.setId("id1"); c.setTenantId("t1"); c.setCartId("c1"); c.setCustomerId("cust1");
        c.setCartType(Cart.CartType.CUSTOMER); c.setStatus(Cart.CartStatus.ACTIVE);
        c.setItems(new ArrayList<>()); c.setSubtotal(BigDecimal.TEN);
        c.setTaxAmount(BigDecimal.ONE); c.setDiscountAmount(BigDecimal.ZERO);
        c.setTotalAmount(BigDecimal.TEN); c.setCurrency("USD");
        c.setCreatedAt(FIXED); c.setUpdatedAt(FIXED);
        c.setExpiresAt(FIXED); c.setCreatedBy("user1"); c.setUpdatedBy("user1");
        return c;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        Cart c1 = createFull(); Cart c2 = createFull(); c2.setCartId("other");
        assertThat(c1).isNotEqualTo(c2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void equals_self() { Cart c = createFull(); assertThat(c).isEqualTo(c); }
    @Test void hashCode_consistency() { Cart c = createFull(); assertThat(c.hashCode()).isEqualTo(c.hashCode()); }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("Cart"); }
    @Test void canEqual() { assertThat(createFull().canEqual(new Cart())).isTrue(); }
}
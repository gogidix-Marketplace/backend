package com.gogidix.ecommerce.cart.application.dto;

import com.gogidix.ecommerce.cart.domain.model.Cart;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class CartDtoTest {

    @Test void cartDto_allFields() {
        Instant now = Instant.now();
        CartSummaryDto summary = new CartSummaryDto(2, 5, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.valueOf(12));
        CartItemDto item = new CartItemDto("sku1", "p1", "W", 3, BigDecimal.TEN, BigDecimal.valueOf(30), "USD", null, now, now);
        CartDto dto = new CartDto("id1", "c1", Cart.CartStatus.ACTIVE, "USD", "web",
            List.of(item), summary, now, now, now, 0);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.customerId()).isEqualTo("c1");
        assertThat(dto.status()).isEqualTo(Cart.CartStatus.ACTIVE);
        assertThat(dto.items()).hasSize(1);
        assertThat(dto.summary().subtotal()).isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test void cartDto_nulls() {
        CartDto dto = new CartDto(null, null, null, null, null, null, null, null, null, null, null);
        assertThat(dto.id()).isNull(); assertThat(dto.items()).isNull();
    }

    @Test void cartResponse_allFields() {
        Instant now = Instant.now();
        CartResponse r = new CartResponse("id1", "cart1", "c1", Cart.CartStatus.ACTIVE,
            "USD", "web", List.of(), null, now, now, now, 1);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.cartId()).isEqualTo("cart1");
    }

    @Test void cartItemDto_allFields() {
        Instant now = Instant.now();
        CartItemDto dto = new CartItemDto("sku1", "p1", "W", 2, BigDecimal.TEN, BigDecimal.valueOf(20), "USD", Map.of("color","red"), now, now);
        assertThat(dto.sku()).isEqualTo("sku1");
        assertThat(dto.quantity()).isEqualTo(2);
        assertThat(dto.totalPrice()).isEqualByComparingTo("20");
        assertThat(dto.attributes()).containsEntry("color","red");
    }

    @Test void cartSummaryDto_allFields() {
        CartSummaryDto dto = new CartSummaryDto(3, 10, BigDecimal.valueOf(100), BigDecimal.TEN, BigDecimal.valueOf(5), BigDecimal.valueOf(15), BigDecimal.valueOf(110));
        assertThat(dto.itemCount()).isEqualTo(3);
        assertThat(dto.totalQuantity()).isEqualTo(10);
        assertThat(dto.subtotal()).isEqualByComparingTo("100");
    }

    @Test void createCartRequest() {
        CreateCartRequest req = new CreateCartRequest("c1", "USD", "web");
        assertThat(req.customerId()).isEqualTo("c1");
        assertThat(req.currency()).isEqualTo("USD");
        assertThat(req.channelId()).isEqualTo("web");
    }

    @Test void addItemRequest() {
        AddItemRequest req = new AddItemRequest("sku1", "p1", "W", 3, BigDecimal.TEN, "USD", null);
        assertThat(req.sku()).isEqualTo("sku1");
        assertThat(req.quantity()).isEqualTo(3);
    }

    @Test void updateItemQuantityRequest() {
        UpdateItemQuantityRequest req = new UpdateItemQuantityRequest(5);
        assertThat(req.quantity()).isEqualTo(5);
    }

    @Test void applyDiscountRequest() {
        ApplyDiscountRequest req = new ApplyDiscountRequest("SAVE10");
        assertThat(req.discountCode()).isEqualTo("SAVE10");
    }

    @Test void applyShippingRequest() {
        ApplyShippingRequest req = new ApplyShippingRequest("express");
        assertThat(req.shippingMethodId()).isEqualTo("express");
    }

    @Test void dto_equality() {
        CartSummaryDto a = new CartSummaryDto(1, 2, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE);
        CartSummaryDto b = new CartSummaryDto(1, 2, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE);
        assertThat(a).isEqualTo(b); assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
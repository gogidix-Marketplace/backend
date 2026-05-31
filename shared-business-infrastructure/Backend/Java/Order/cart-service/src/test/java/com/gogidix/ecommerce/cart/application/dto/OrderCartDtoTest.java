package com.gogidix.ecommerce.cart.application.dto;
import org.junit.jupiter.api.Test; import java.math.BigDecimal; import java.time.Instant; import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
class OrderCartDtoTest {
    @Test void cartDto_allFields() {
        Instant now = Instant.now();
        CartDto dto = new CartDto("id1", "t1", "c1", List.of(), BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.valueOf(11), now, now);
        assertThat(dto.id()).isEqualTo("id1"); assertThat(dto.customerId()).isEqualTo("c1");
        assertThat(dto.subtotal()).isEqualByComparingTo("10"); assertThat(dto.totalAmount()).isEqualByComparingTo("11");
    }
    @Test void cartResponse_fromDto() {
        CartDto dto = new CartDto("id1", "t1", "c1", null, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.valueOf(11), null, null);
        CartResponse r = CartResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1"); assertThat(r.totalAmount()).isEqualByComparingTo("11");
    }
    @Test void cartItemDto() {
        CartItemDto dto = new CartItemDto("i1", "p1", "sku1", "Widget", "http://img", BigDecimal.TEN, 2, BigDecimal.valueOf(20), "v1", true);
        assertThat(dto.itemId()).isEqualTo("i1"); assertThat(dto.quantity()).isEqualTo(2);
        assertThat(dto.lineTotal()).isEqualByComparingTo("20"); assertThat(dto.inStock()).isTrue();
    }
    @Test void addCartItemRequest() {
        AddCartItemRequest req = new AddCartItemRequest("p1", "sku1", "W", "http://img", BigDecimal.TEN, 3, "v1");
        assertThat(req.productId()).isEqualTo("p1"); assertThat(req.quantity()).isEqualTo(3);
    }
    @Test void updateCartItemRequest() {
        UpdateCartItemRequest req = new UpdateCartItemRequest("p1", 5);
        assertThat(req.productId()).isEqualTo("p1"); assertThat(req.quantity()).isEqualTo(5);
    }
    @Test void equality() {
        CartDto a = new CartDto("id", "t", "c", null, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, null, null);
        CartDto b = new CartDto("id", "t", "c", null, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, null, null);
        assertThat(a).isEqualTo(b);
    }
}
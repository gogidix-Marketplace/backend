package com.gogidix.ecommerce.checkout.application.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class CheckoutDtoTest {

    @Test void checkoutDto_allFields() {
        Instant now = Instant.now();
        Address ship = new Address("123 Main", "NYC", "NY", "10001", "US");
        Address bill = new Address("456 Oak", "LA", "CA", "90001", "US");
        CheckoutDto dto = new CheckoutDto("id1", "t1", "c1", "cart1",
            ship, bill, "card", BigDecimal.valueOf(100), BigDecimal.TEN,
            BigDecimal.ONE, BigDecimal.valueOf(111), "INITIATED", now, now);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.customerId()).isEqualTo("c1");
        assertThat(dto.cartId()).isEqualTo("cart1");
        assertThat(dto.shippingAddress()).isEqualTo(ship);
        assertThat(dto.billingAddress()).isEqualTo(bill);
        assertThat(dto.paymentMethod()).isEqualTo("card");
        assertThat(dto.subtotal()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(dto.tax()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(dto.shipping()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(dto.total()).isEqualByComparingTo(BigDecimal.valueOf(111));
        assertThat(dto.status()).isEqualTo("INITIATED");
        assertThat(dto.createdAt()).isEqualTo(now);
    }

    @Test void checkoutDto_nullFields() {
        CheckoutDto dto = new CheckoutDto(null, null, null, null, null, null,
            null, null, null, null, null, null, null, null);
        assertThat(dto.id()).isNull();
        assertThat(dto.status()).isNull();
    }

    @Test void checkoutDto_equality() {
        CheckoutDto a = new CheckoutDto("id", "t", "c", "cart", null, null,
            null, null, null, null, null, null, null, null);
        CheckoutDto b = new CheckoutDto("id", "t", "c", "cart", null, null,
            null, null, null, null, null, null, null, null);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test void address_allFields() {
        Address a = new Address("123 Main", "NYC", "NY", "10001", "US");
        assertThat(a.street()).isEqualTo("123 Main");
        assertThat(a.city()).isEqualTo("NYC");
        assertThat(a.state()).isEqualTo("NY");
        assertThat(a.postalCode()).isEqualTo("10001");
        assertThat(a.country()).isEqualTo("US");
    }

    @Test void address_nulls() {
        Address a = new Address(null, null, null, null, null);
        assertThat(a.street()).isNull();
    }

    @Test void address_equality() {
        Address a = new Address("s", "c", "st", "p", "co");
        Address b = new Address("s", "c", "st", "p", "co");
        assertThat(a).isEqualTo(b);
    }

    @Test void checkoutResponse_fromDto() {
        Instant now = Instant.now();
        CheckoutDto dto = new CheckoutDto("id1", "t1", "c1", "cart1", null, null,
            "card", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO,
            BigDecimal.valueOf(11), "COMPLETED", now, now);
        CheckoutResponse r = CheckoutResponse.from(dto);
        assertThat(r.id()).isEqualTo("id1");
        assertThat(r.tenantId()).isEqualTo("t1");
        assertThat(r.customerId()).isEqualTo("c1");
        assertThat(r.status()).isEqualTo("COMPLETED");
        assertThat(r.total()).isEqualByComparingTo(BigDecimal.valueOf(11));
        assertThat(r.createdAt()).isEqualTo(now);
    }

    @Test void checkoutResponse_direct() {
        Instant now = Instant.now();
        CheckoutResponse r = new CheckoutResponse("id2", "t2", "c2", "INITIATED", BigDecimal.ONE, now);
        assertThat(r.id()).isEqualTo("id2");
        assertThat(r.status()).isEqualTo("INITIATED");
    }

    @Test void createCheckoutRequest_allFields() {
        Address ship = new Address("s", "c", "st", "p", "co");
        CreateCheckoutRequest req = new CreateCheckoutRequest("cart1", ship, null, "card");
        assertThat(req.cartId()).isEqualTo("cart1");
        assertThat(req.shippingAddress()).isEqualTo(ship);
        assertThat(req.billingAddress()).isNull();
        assertThat(req.paymentMethod()).isEqualTo("card");
    }

    @Test void createCheckoutRequest_nulls() {
        CreateCheckoutRequest req = new CreateCheckoutRequest(null, null, null, null);
        assertThat(req.cartId()).isNull();
        assertThat(req.paymentMethod()).isNull();
    }
}
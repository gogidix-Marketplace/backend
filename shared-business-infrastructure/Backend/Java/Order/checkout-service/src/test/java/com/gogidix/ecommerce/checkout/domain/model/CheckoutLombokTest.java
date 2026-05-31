package com.gogidix.ecommerce.checkout.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CheckoutLombokTest {
    private static final Instant FIXED = Instant.parse("2026-01-01T00:00:00Z");
    private Checkout createFull() {
        Instant now = FIXED;
        Checkout c = new Checkout();
        c.setId("id1"); c.setTenantId("t1"); c.setCheckoutId("co1"); c.setCustomerId("cust1");
        c.setCartId("cart1"); c.setStatus(Checkout.CheckoutStatus.INITIATED); c.setOrderId("ord1");
        c.setShippingMethod("express"); c.setShippingAmount(BigDecimal.ONE); c.setPaymentMethod("card");
        c.setSubtotal(BigDecimal.TEN); c.setTaxAmount(BigDecimal.ONE);
        c.setDiscountAmount(BigDecimal.ZERO); c.setTotalAmount(BigDecimal.TEN);
        c.setCurrency("USD"); c.setPromoCode("SAVE10"); c.setItems(new ArrayList<>());
        c.setNotes("note"); c.setCreatedAt(now); c.setUpdatedAt(now); c.setCompletedAt(now);

        Checkout.ShippingAddress sa = new Checkout.ShippingAddress();
        sa.setFirstName("John"); sa.setLastName("Doe"); sa.setCompany("Co");
        sa.setAddressLine1("123 Main"); sa.setAddressLine2("Apt 1"); sa.setCity("NYC");
        sa.setState("NY"); sa.setPostalCode("10001"); sa.setCountry("US"); sa.setPhone("555");
        c.setShippingAddress(sa);

        Checkout.BillingAddress ba = new Checkout.BillingAddress();
        ba.setFirstName("John"); ba.setLastName("Doe"); ba.setCompany("Co");
        ba.setAddressLine1("123 Main"); ba.setAddressLine2("Apt 1"); ba.setCity("NYC");
        ba.setState("NY"); ba.setPostalCode("10001"); ba.setCountry("US");
        c.setBillingAddress(ba);

        Checkout.PaymentInfo pi = new Checkout.PaymentInfo();
        pi.setCardLast4("1234"); pi.setCardType("visa"); pi.setTransactionId("tx1");
        c.setPaymentInfo(pi);
        return c;
    }

    @Test void equals_same() { assertThat(createFull()).isEqualTo(createFull()); }
    @Test void equals_different() {
        Checkout c1 = createFull(); Checkout c2 = createFull(); c2.setId("other");
        assertThat(c1).isNotEqualTo(c2);
    }
    @Test void equals_null() { assertThat(createFull()).isNotEqualTo(null); }
    @Test void equals_differentType() { assertThat(createFull()).isNotEqualTo("str"); }
    @Test void equals_self() { Checkout c = createFull(); assertThat(c).isEqualTo(c); }
    @Test void hashCode_consistency() {
        Checkout c = createFull(); int h1 = c.hashCode(); int h2 = c.hashCode();
        assertThat(h1).isEqualTo(h2);
    }
    @Test void toString_notNull() { assertThat(createFull().toString()).contains("Checkout"); }
    @Test void canEqual() {
        assertThat(createFull().canEqual(new Checkout())).isTrue();
        assertThat(createFull().canEqual("str")).isFalse();
    }
    @Test void shippingAddress_equals() {
        Checkout.ShippingAddress a1 = new Checkout.ShippingAddress();
        a1.setFirstName("John"); a1.setCity("NYC");
        Checkout.ShippingAddress a2 = new Checkout.ShippingAddress();
        a2.setFirstName("John"); a2.setCity("NYC");
        assertThat(a1).isEqualTo(a2);
        a2.setCity("LA");
        assertThat(a1).isNotEqualTo(a2);
    }
    @Test void billingAddress_equals() {
        Checkout.BillingAddress a1 = new Checkout.BillingAddress();
        a1.setFirstName("John"); a1.setCity("NYC");
        Checkout.BillingAddress a2 = new Checkout.BillingAddress();
        a2.setFirstName("John"); a2.setCity("NYC");
        assertThat(a1).isEqualTo(a2);
    }
    @Test void paymentInfo_equals() {
        Checkout.PaymentInfo p1 = new Checkout.PaymentInfo();
        p1.setCardLast4("1234"); p1.setCardType("visa");
        Checkout.PaymentInfo p2 = new Checkout.PaymentInfo();
        p2.setCardLast4("1234"); p2.setCardType("visa");
        assertThat(p1).isEqualTo(p2);
        p2.setCardLast4("5678");
        assertThat(p1).isNotEqualTo(p2);
    }
}
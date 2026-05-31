package com.gogidix.ecommerce.checkout.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CheckoutTest {

    private Checkout createCheckout() {
        Checkout c = new Checkout();
        c.setId("id1");
        c.setTenantId("t1");
        c.setCheckoutId("co-123");
        c.setCustomerId("cust1");
        c.setCartId("cart1");
        c.setStatus(Checkout.CheckoutStatus.INITIATED);
        c.setOrderId("order1");
        c.setShippingMethod("express");
        c.setShippingAmount(BigDecimal.TEN);
        c.setPaymentMethod("card");
        c.setSubtotal(BigDecimal.valueOf(100));
        c.setTaxAmount(BigDecimal.valueOf(10));
        c.setDiscountAmount(BigDecimal.valueOf(5));
        c.setTotalAmount(BigDecimal.valueOf(115));
        c.setCurrency("USD");
        c.setPromoCode("SAVE10");
        c.setNotes("fragile");
        c.setCreatedAt(Instant.now());
        c.setUpdatedAt(Instant.now());
        c.setCompletedAt(Instant.now());
        return c;
    }

    @Test void allFields() {
        Checkout c = createCheckout();
        assertThat(c.getId()).isEqualTo("id1");
        assertThat(c.getTenantId()).isEqualTo("t1");
        assertThat(c.getCheckoutId()).isEqualTo("co-123");
        assertThat(c.getCustomerId()).isEqualTo("cust1");
        assertThat(c.getCartId()).isEqualTo("cart1");
        assertThat(c.getStatus()).isEqualTo(Checkout.CheckoutStatus.INITIATED);
        assertThat(c.getOrderId()).isEqualTo("order1");
        assertThat(c.getShippingMethod()).isEqualTo("express");
        assertThat(c.getShippingAmount()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(c.getPaymentMethod()).isEqualTo("card");
        assertThat(c.getSubtotal()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(c.getTaxAmount()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(c.getDiscountAmount()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(c.getTotalAmount()).isEqualByComparingTo(BigDecimal.valueOf(115));
        assertThat(c.getCurrency()).isEqualTo("USD");
        assertThat(c.getPromoCode()).isEqualTo("SAVE10");
        assertThat(c.getNotes()).isEqualTo("fragile");
        assertThat(c.getCreatedAt()).isNotNull();
        assertThat(c.getUpdatedAt()).isNotNull();
        assertThat(c.getCompletedAt()).isNotNull();
    }

    @Test void shippingAddress() {
        Checkout.ShippingAddress a = new Checkout.ShippingAddress();
        a.setFirstName("John"); a.setLastName("Doe"); a.setCompany("Acme");
        a.setAddressLine1("123 Main"); a.setAddressLine2("Apt 4");
        a.setCity("NYC"); a.setState("NY"); a.setPostalCode("10001");
        a.setCountry("US"); a.setPhone("555-1234");
        assertThat(a.getFirstName()).isEqualTo("John");
        assertThat(a.getLastName()).isEqualTo("Doe");
        assertThat(a.getCompany()).isEqualTo("Acme");
        assertThat(a.getAddressLine1()).isEqualTo("123 Main");
        assertThat(a.getAddressLine2()).isEqualTo("Apt 4");
        assertThat(a.getCity()).isEqualTo("NYC");
        assertThat(a.getState()).isEqualTo("NY");
        assertThat(a.getPostalCode()).isEqualTo("10001");
        assertThat(a.getCountry()).isEqualTo("US");
        assertThat(a.getPhone()).isEqualTo("555-1234");
    }

    @Test void billingAddress() {
        Checkout.BillingAddress a = new Checkout.BillingAddress();
        a.setFirstName("Jane"); a.setLastName("Smith");
        a.setAddressLine1("456 Oak"); a.setCity("LA");
        a.setState("CA"); a.setPostalCode("90001"); a.setCountry("US");
        assertThat(a.getFirstName()).isEqualTo("Jane");
        assertThat(a.getCity()).isEqualTo("LA");
    }

    @Test void paymentInfo() {
        Checkout.PaymentInfo p = new Checkout.PaymentInfo();
        p.setCardLast4("4242"); p.setCardType("visa"); p.setTransactionId("tx-1");
        assertThat(p.getCardLast4()).isEqualTo("4242");
        assertThat(p.getCardType()).isEqualTo("visa");
        assertThat(p.getTransactionId()).isEqualTo("tx-1");
    }

    @Test void checkoutItem() {
        Checkout.CheckoutItem item = new Checkout.CheckoutItem();
        item.setProductId("p1"); item.setSku("sku1"); item.setName("Widget");
        item.setUnitPrice(BigDecimal.valueOf(9.99)); item.setQuantity(3);
        item.setLineTotal(BigDecimal.valueOf(29.97));
        assertThat(item.getProductId()).isEqualTo("p1");
        assertThat(item.getSku()).isEqualTo("sku1");
        assertThat(item.getName()).isEqualTo("Widget");
        assertThat(item.getUnitPrice()).isEqualByComparingTo("9.99");
        assertThat(item.getQuantity()).isEqualTo(3);
        assertThat(item.getLineTotal()).isEqualByComparingTo("29.97");
    }

    @Test void statusEnum() {
        assertThat(Checkout.CheckoutStatus.valueOf("INITIATED")).isEqualTo(Checkout.CheckoutStatus.INITIATED);
        assertThat(Checkout.CheckoutStatus.valueOf("COMPLETED")).isEqualTo(Checkout.CheckoutStatus.COMPLETED);
        assertThat(Checkout.CheckoutStatus.valueOf("FAILED")).isEqualTo(Checkout.CheckoutStatus.FAILED);
        assertThat(Checkout.CheckoutStatus.valueOf("CANCELLED")).isEqualTo(Checkout.CheckoutStatus.CANCELLED);
        assertThat(Checkout.CheckoutStatus.values()).hasSize(8);
    }

    @Test void itemsList() {
        Checkout c = new Checkout();
        Checkout.CheckoutItem item = new Checkout.CheckoutItem();
        item.setProductId("p1"); item.setName("Widget"); item.setQuantity(2);
        c.setItems(List.of(item));
        assertThat(c.getItems()).hasSize(1);
        assertThat(c.getItems().get(0).getName()).isEqualTo("Widget");
    }

    @Test void nullDefaults() {
        Checkout c = new Checkout();
        assertThat(c.getId()).isNull();
        assertThat(c.getStatus()).isNull();
        assertThat(c.getItems()).isNull();
        assertThat(c.getCompletedAt()).isNull();
    }
}
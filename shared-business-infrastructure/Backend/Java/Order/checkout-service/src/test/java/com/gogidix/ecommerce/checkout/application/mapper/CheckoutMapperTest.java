package com.gogidix.ecommerce.checkout.application.mapper;

import com.gogidix.ecommerce.checkout.application.dto.*;
import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

class CheckoutMapperTest {

    private CheckoutMapper mapper = new CheckoutMapper();

    @BeforeEach void setUp() {
        RequestContextHolder.set(RequestContext.builder()
            .tenantId("t1").customerId("c1").userId("u1")
            .correlationId("corr1").build());
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Checkout createEntity() {
        Checkout c = new Checkout();
        c.setId("id1"); c.setTenantId("t1"); c.setCustomerId("c1");
        c.setCartId("cart1"); c.setPaymentMethod("card");
        c.setSubtotal(BigDecimal.valueOf(100)); c.setTaxAmount(BigDecimal.TEN);
        c.setShippingAmount(BigDecimal.ONE); c.setTotalAmount(BigDecimal.valueOf(111));
        c.setStatus(Checkout.CheckoutStatus.COMPLETED);
        c.setCreatedAt(Instant.parse("2024-01-01T00:00:00Z"));
        c.setUpdatedAt(Instant.parse("2024-06-15T12:00:00Z"));
        return c;
    }

    @Test void toDto_mapsFields() {
        Checkout entity = createEntity();
        CheckoutDto dto = mapper.toDto(entity);
        assertThat(dto.id()).isEqualTo("id1");
        assertThat(dto.tenantId()).isEqualTo("t1");
        assertThat(dto.customerId()).isEqualTo("c1");
        assertThat(dto.cartId()).isEqualTo("cart1");
        assertThat(dto.paymentMethod()).isEqualTo("card");
        assertThat(dto.subtotal()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(dto.status()).isEqualTo("COMPLETED");
        assertThat(dto.createdAt()).isEqualTo(Instant.parse("2024-01-01T00:00:00Z"));
    }

    @Test void toDto_nullStatus() {
        Checkout entity = createEntity();
        entity.setStatus(null);
        CheckoutDto dto = mapper.toDto(entity);
        assertThat(dto.status()).isNull();
    }

    @Test void toEntity_fromRequest() {
        Address addr = new Address("123 Main", "NYC", "NY", "10001", "US");
        CreateCheckoutRequest req = new CreateCheckoutRequest("cart1", addr, null, "card");
        Checkout entity = mapper.toEntity(req);
        assertThat(entity.getTenantId()).isEqualTo("t1");
        assertThat(entity.getCustomerId()).isEqualTo("c1");
        assertThat(entity.getCartId()).isEqualTo("cart1");
        assertThat(entity.getPaymentMethod()).isEqualTo("card");
    }

    @Test void toResponse_fromDto() {
        Instant now = Instant.now();
        CheckoutDto dto = new CheckoutDto("id1", "t1", "c1", "cart1", null, null,
            "card", BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO,
            BigDecimal.valueOf(11), "COMPLETED", now, now);
        CheckoutResponse resp = mapper.toResponse(dto);
        assertThat(resp.id()).isEqualTo("id1");
        assertThat(resp.status()).isEqualTo("COMPLETED");
        assertThat(resp.total()).isEqualByComparingTo(BigDecimal.valueOf(11));
    }
}
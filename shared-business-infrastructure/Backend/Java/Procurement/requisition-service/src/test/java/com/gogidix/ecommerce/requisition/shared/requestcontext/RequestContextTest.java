package com.gogidix.ecommerce.requisition.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextTest {

    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    @Test void builder_minimal() {
        RequestContext ctx = RequestContext.builder().tenantId("t1").build();
        assertThat(ctx.tenantId()).isEqualTo("t1");
        assertThat(ctx.correlationId()).isNotNull();
        assertThat(ctx.metadata()).isEmpty();
    }
    @Test void builder_full() {
        RequestContext ctx = RequestContext.builder()
            .tenantId("t1").customerId("c1").userId("u1")
            .correlationId("corr1").metadata(Map.of("k","v")).build();
        assertThat(ctx.tenantId()).isEqualTo("t1");
        assertThat(ctx.customerId()).isEqualTo("c1");
        assertThat(ctx.userId()).isEqualTo("u1");
        assertThat(ctx.correlationId()).isEqualTo("corr1");
        assertThat(ctx.metadata()).containsEntry("k","v");
    }
    @Test void builder_missingTenantId() {
        assertThatThrownBy(() -> RequestContext.builder().build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("tenantId");
    }
    @Test void holder_setAndGet() {
        RequestContext ctx = RequestContext.builder().tenantId("t1").build();
        RequestContextHolder.set(ctx);
        assertThat(RequestContextHolder.get()).isEqualTo(ctx);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("t1");
        assertThat(RequestContextHolder.isSet()).isTrue();
    }
    @Test void holder_getWithoutSet() {
        assertThatThrownBy(() -> RequestContextHolder.get())
            .isInstanceOf(IllegalStateException.class);
    }
    @Test void holder_clear() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").build());
        RequestContextHolder.clear();
        assertThat(RequestContextHolder.isSet()).isFalse();
    }
    @Test void holder_convenienceMethods() {
        RequestContext ctx = RequestContext.builder()
            .tenantId("t1").customerId("c1").userId("u1")
            .correlationId("corr1").build();
        RequestContextHolder.set(ctx);
        assertThat(RequestContextHolder.getCustomerId()).isEqualTo("c1");
        assertThat(RequestContextHolder.getUserId()).isEqualTo("u1");
        assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr1");
    }
}
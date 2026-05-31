package com.gogidix.ecommerce.notification.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RequestContext Tests")
class RequestContextTest {

    @AfterEach
    void tearDown() { RequestContextHolder.clear(); }

    @Test
    @DisplayName("Should build with all fields")
    void shouldBuildWithAllFields() {
        RequestContext ctx = RequestContext.builder()
                .tenantId("t1")
                .userId("u1")
                .correlationId("c1")
                .metadata(Map.of("key", "val"))
                .build();
        assertThat(ctx.tenantId()).isEqualTo("t1");
        assertThat(ctx.userId()).isEqualTo("u1");
        assertThat(ctx.correlationId()).isEqualTo("c1");
        assertThat(ctx.metadata()).containsEntry("key", "val");
    }

    @Test
    @DisplayName("Should generate correlation ID if null")
    void shouldGenerateCorrelationId() {
        RequestContext ctx = RequestContext.builder()
                .tenantId("t1")
                .correlationId(null)
                .build();
        assertThat(ctx.correlationId()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Should use empty map if metadata null")
    void shouldUseEmptyMap() {
        RequestContext ctx = RequestContext.builder()
                .tenantId("t1")
                .metadata(null)
                .build();
        assertThat(ctx.metadata()).isEmpty();
    }

    @Test
    @DisplayName("Should throw if tenantId null")
    void shouldThrowIfTenantIdNull() {
        assertThatThrownBy(() -> RequestContext.builder().build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Holder set and get should work")
    void holderSetAndGet() {
        RequestContext ctx = RequestContext.builder().tenantId("t1").build();
        RequestContextHolder.set(ctx);
        assertThat(RequestContextHolder.get()).isEqualTo(ctx);
        assertThat(RequestContextHolder.isInitialized()).isTrue();
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("t1");
    }

    @Test
    @DisplayName("Holder clear should work")
    void holderClear() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").build());
        RequestContextHolder.clear();
        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    @DisplayName("Holder get without set should throw")
    void holderGetWithoutSet() {
        assertThatThrownBy(() -> RequestContextHolder.get())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Should get userId and correlationId from holder")
    void holderGetters() {
        RequestContext ctx = RequestContext.builder()
                .tenantId("t1").userId("u1").correlationId("c1").build();
        RequestContextHolder.set(ctx);
        assertThat(RequestContextHolder.getUserId()).isEqualTo("u1");
        assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("c1");
    }
}

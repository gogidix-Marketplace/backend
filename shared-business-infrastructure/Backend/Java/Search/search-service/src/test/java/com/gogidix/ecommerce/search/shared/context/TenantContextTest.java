package com.gogidix.ecommerce.search.shared.context;

import com.gogidix.ecommerce.search.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.search.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TenantContext Tests")
class TenantContextTest {

    private final TenantContext tenantContext = new TenantContext();

    @AfterEach
    void tearDown() { RequestContextHolder.clear(); }

    @Test
    @DisplayName("Should get current tenant ID")
    void shouldGetCurrentTenantId() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").build());
        assertThat(tenantContext.getCurrentTenantId()).isEqualTo("t1");
    }

    @Test
    @DisplayName("Should get current correlation ID")
    void shouldGetCurrentCorrelationId() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").correlationId("c1").build());
        assertThat(tenantContext.getCurrentCorrelationId()).isEqualTo("c1");
    }

    @Test
    @DisplayName("Should check if initialized")
    void shouldCheckInitialized() {
        assertThat(tenantContext.isInitialized()).isFalse();
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").build());
        assertThat(tenantContext.isInitialized()).isTrue();
    }
}

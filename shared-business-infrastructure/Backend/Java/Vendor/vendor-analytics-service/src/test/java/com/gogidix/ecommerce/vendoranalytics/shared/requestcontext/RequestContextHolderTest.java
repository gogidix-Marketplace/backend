package com.gogidix.ecommerce.vendor.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextHolderTest {

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    void setAndGet_shouldWorkCorrectly() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .userId("user456")
                .correlationId("corr789")
                .build();

        RequestContextHolder.set(context);
        RequestContext retrieved = RequestContextHolder.get();

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.tenantId()).isEqualTo("tenant123");
        assertThat(retrieved.userId()).isEqualTo("user456");
        assertThat(retrieved.correlationId()).isEqualTo("corr789");
    }

    @Test
    void get_shouldThrowException_whenContextNotSet() {
        assertThatThrownBy(RequestContextHolder::get)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not initialized");
    }

    @Test
    void isInitialized_shouldReturnTrue_whenContextSet() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .build();

        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.isInitialized()).isTrue();
    }

    @Test
    void isInitialized_shouldReturnFalse_whenContextNotSet() {
        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void clear_shouldRemoveContext() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .build();

        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.isInitialized()).isTrue();

        RequestContextHolder.clear();
        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void getTenantId_shouldReturnTenantId() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .build();

        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant123");
    }

    @Test
    void getUserId_shouldReturnUserId() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .userId("user456")
                .build();

        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.getUserId()).isEqualTo("user456");
    }

    @Test
    void getCorrelationId_shouldReturnCorrelationId() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .correlationId("corr789")
                .build();

        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr789");
    }
}

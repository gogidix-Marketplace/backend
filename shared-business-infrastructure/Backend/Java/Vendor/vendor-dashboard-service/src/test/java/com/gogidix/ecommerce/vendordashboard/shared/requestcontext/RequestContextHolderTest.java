package com.gogidix.ecommerce.vendor.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextHolderTest {

    private RequestContext testContext;

    @BeforeEach
    void setUp() {
        testContext = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .correlationId("corr-123")
                .build();
        RequestContextHolder.set(testContext);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    void setAndGet_shouldReturnSameContext() {
        RequestContext retrieved = RequestContextHolder.get();

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.tenantId()).isEqualTo("tenant-123");
    }

    @Test
    void get_withoutSet_shouldThrowException() {
        RequestContextHolder.clear();
        assertThatThrownBy(() -> RequestContextHolder.get())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void clear_shouldRemoveContext() {
        assertThat(RequestContextHolder.isInitialized()).isTrue();

        RequestContextHolder.clear();

        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void getTenantId_shouldReturnTenantId() {
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-123");
    }

    @Test
    void getUserId_shouldReturnUserId() {
        assertThat(RequestContextHolder.getUserId()).isEqualTo("user-123");
    }
}

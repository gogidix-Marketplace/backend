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
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    void setAndGet_shouldReturnSameContext() {
        RequestContextHolder.set(testContext);

        RequestContext retrieved = RequestContextHolder.get();

        assertThat(retrieved).isNotNull();
        assertThat(retrieved.tenantId()).isEqualTo("tenant-123");
        assertThat(retrieved.userId()).isEqualTo("user-123");
        assertThat(retrieved.correlationId()).isEqualTo("corr-123");
    }

    @Test
    void get_withoutSet_shouldThrowException() {
        // Clear any existing context
        RequestContextHolder.clear();

        assertThatThrownBy(() -> RequestContextHolder.get())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not initialized");
    }

    @Test
    void clear_shouldRemoveContext() {
        RequestContextHolder.set(testContext);
        assertThat(RequestContextHolder.isInitialized()).isTrue();

        RequestContextHolder.clear();

        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void isInitialized_shouldReturnTrueWhenSet() {
        RequestContextHolder.set(testContext);

        assertThat(RequestContextHolder.isInitialized()).isTrue();
    }

    @Test
    void isInitialized_shouldReturnFalseWhenNotSet() {
        RequestContextHolder.clear();

        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void getTenantId_shouldReturnTenantId() {
        RequestContextHolder.set(testContext);

        String tenantId = RequestContextHolder.getTenantId();

        assertThat(tenantId).isEqualTo("tenant-123");
    }

    @Test
    void getUserId_shouldReturnUserId() {
        RequestContextHolder.set(testContext);

        String userId = RequestContextHolder.getUserId();

        assertThat(userId).isEqualTo("user-123");
    }

    @Test
    void getCorrelationId_shouldReturnCorrelationId() {
        RequestContextHolder.set(testContext);

        String correlationId = RequestContextHolder.getCorrelationId();

        assertThat(correlationId).isEqualTo("corr-123");
    }

    @Test
    void set_shouldOverrideExistingContext() {
        RequestContext context1 = RequestContext.builder()
                .tenantId("tenant-1")
                .userId("user-1")
                .build();

        RequestContext context2 = RequestContext.builder()
                .tenantId("tenant-2")
                .userId("user-2")
                .build();

        RequestContextHolder.set(context1);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-1");

        RequestContextHolder.set(context2);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-2");
    }

    @Test
    void multipleThreads_shouldHaveSeparateContexts() throws InterruptedException {
        RequestContext context1 = RequestContext.builder()
                .tenantId("tenant-1")
                .build();

        RequestContext context2 = RequestContext.builder()
                .tenantId("tenant-2")
                .build();

        Thread thread1 = new Thread(() -> {
            RequestContextHolder.set(context1);
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-1");
        });

        Thread thread2 = new Thread(() -> {
            RequestContextHolder.set(context2);
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-2");
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Main thread should not have a context
        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }

    @Test
    void clear_shouldBeIdempotent() {
        RequestContextHolder.set(testContext);
        RequestContextHolder.clear();
        assertThat(RequestContextHolder.isInitialized()).isFalse();

        // Second clear should not throw
        RequestContextHolder.clear();
        assertThat(RequestContextHolder.isInitialized()).isFalse();
    }
}

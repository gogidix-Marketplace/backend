package com.gogidix.customersupport.ticketmanagement.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RequestContextHolder Tests")
class RequestContextHolderTest {

    @BeforeEach
    void setUp() {
        RequestContextHolder.clear();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    @DisplayName("set should store context")
    void set_ShouldStoreContext() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .build();

        // When
        RequestContextHolder.set(context);

        // Then
        assertThat(RequestContextHolder.get()).isPresent();
        assertThat(RequestContextHolder.get().get()).isSameAs(context);
    }

    @Test
    @DisplayName("get should return empty when not set")
    void get_ShouldReturnEmptyWhenNotSet() {
        // When
        var result = RequestContextHolder.get();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("require should throw exception when not set")
    void require_ShouldThrowException() {
        // When/Then
        assertThatThrownBy(() -> RequestContextHolder.require())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("require should return context when set")
    void require_ShouldReturnContext() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .build();
        RequestContextHolder.set(context);

        // When
        RequestContext result = RequestContextHolder.require();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tenantId()).isEqualTo("tenant-123");
    }

    @Test
    @DisplayName("getTenantId should return tenantId")
    void getTenantId_ShouldReturnTenantId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-456")
                .build();
        RequestContextHolder.set(context);

        // When
        String tenantId = RequestContextHolder.getTenantId();

        // Then
        assertThat(tenantId).isEqualTo("tenant-456");
    }

    @Test
    @DisplayName("getTenantId should throw exception when not set")
    void getTenantId_ShouldThrowException() {
        // When/Then
        assertThatThrownBy(() -> RequestContextHolder.getTenantId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("getUserId should return userId when set")
    void getUserId_ShouldReturnUserId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-789")
                .build();
        RequestContextHolder.set(context);

        // When
        var userId = RequestContextHolder.getUserId();

        // Then
        assertThat(userId).isPresent();
        assertThat(userId.get()).isEqualTo("user-789");
    }

    @Test
    @DisplayName("getUserId should return empty when not set")
    void getUserId_ShouldReturnEmpty() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .build();
        RequestContextHolder.set(context);

        // When
        var userId = RequestContextHolder.getUserId();

        // Then
        assertThat(userId).isEmpty();
    }

    @Test
    @DisplayName("getUserId should return empty when context not set")
    void getUserId_ShouldReturnEmptyWhenContextNotSet() {
        RequestContextHolder.clear();
        var userId = RequestContextHolder.getUserId();
        assertThat(userId).isEmpty();
    }

    @Test
    @DisplayName("getCorrelationId should return correlationId")
    void getCorrelationId_ShouldReturnCorrelationId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .correlationId("corr-456")
                .build();
        RequestContextHolder.set(context);

        // When
        String correlationId = RequestContextHolder.getCorrelationId();

        // Then
        assertThat(correlationId).isEqualTo("corr-456");
    }

    @Test
    @DisplayName("getCorrelationId should throw exception when not set")
    void getCorrelationId_ShouldThrowException() {
        // When/Then
        assertThatThrownBy(() -> RequestContextHolder.getCorrelationId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("clear should remove context")
    void clear_ShouldRemoveContext() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .build();
        RequestContextHolder.set(context);

        // When
        RequestContextHolder.clear();

        // Then
        assertThat(RequestContextHolder.get()).isEmpty();
        assertThatThrownBy(() -> RequestContextHolder.getTenantId())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("set should replace existing context")
    void set_ShouldReplaceExisting() {
        // Given
        RequestContext context1 = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-1")
                .build();
        RequestContextHolder.set(context1);

        RequestContext context2 = RequestContext.builder()
                .tenantId("tenant-456")
                .userId("user-2")
                .build();

        // When
        RequestContextHolder.set(context2);

        // Then
        assertThat(RequestContextHolder.get()).isPresent();
        assertThat(RequestContextHolder.get().get().tenantId()).isEqualTo("tenant-456");
        assertThat(RequestContextHolder.get().get().userId()).isEqualTo("user-2");
    }

    @Test
    @DisplayName("Should handle null context gracefully")
    void shouldHandleNullContext() {
        // When
        RequestContextHolder.set(null);

        // Then
        assertThat(RequestContextHolder.get()).isEmpty();
    }

    @Test
    @DisplayName("Should support context chaining")
    void shouldSupportContextChaining() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .build();
        RequestContextHolder.set(context);

        // When - multiple gets should return same instance
        var result1 = RequestContextHolder.require();
        var result2 = RequestContextHolder.require();

        // Then
        assertThat(result1).isSameAs(result2);
    }
}

package com.gogidix.customersupport.countrysupportdashboard.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RequestContextHolder Tests")
class RequestContextHolderTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String CORRELATION_ID = "correlation-789";

    @BeforeEach
    void setUp() {
        RequestContextHolder.clear();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clear();
    }

    @Test
    @DisplayName("set should store context in ThreadLocal")
    void set_ShouldStoreContext() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .correlationId(CORRELATION_ID)
                .build();

        // When
        RequestContextHolder.set(context);

        // Then
        Optional<RequestContext> result = RequestContextHolder.get();
        assertThat(result).isPresent();
        assertThat(result.get().tenantId()).isEqualTo(TENANT_ID);
        assertThat(result.get().userId()).isEqualTo(USER_ID);
        assertThat(result.get().correlationId()).isEqualTo(CORRELATION_ID);
    }

    @Test
    @DisplayName("get should return empty when no context is set")
    void get_ShouldReturnEmptyWhenNotSet() {
        // When
        Optional<RequestContext> result = RequestContextHolder.get();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("require should throw exception when no context is set")
    void require_ShouldThrowExceptionWhenNotSet() {
        // When/Then
        assertThatThrownBy(RequestContextHolder::require)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("require should return context when set")
    void require_ShouldReturnContextWhenSet() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .build();
        RequestContextHolder.set(context);

        // When
        RequestContext result = RequestContextHolder.require();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.tenantId()).isEqualTo(TENANT_ID);
    }

    @Test
    @DisplayName("getTenantId should return tenantId when context is set")
    void getTenantId_ShouldReturnTenantId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .build();
        RequestContextHolder.set(context);

        // When
        String result = RequestContextHolder.getTenantId();

        // Then
        assertThat(result).isEqualTo(TENANT_ID);
    }

    @Test
    @DisplayName("getTenantId should throw exception when no context is set")
    void getTenantId_ShouldThrowExceptionWhenNotSet() {
        // When/Then
        assertThatThrownBy(RequestContextHolder::getTenantId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("getUserId should return userId when context is set")
    void getUserId_ShouldReturnUserId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .build();
        RequestContextHolder.set(context);

        // When
        Optional<String> result = RequestContextHolder.getUserId();

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("getUserId should return empty when userId is null")
    void getUserId_ShouldReturnEmptyWhenUserIdIsNull() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .build();
        RequestContextHolder.set(context);

        // When
        Optional<String> result = RequestContextHolder.getUserId();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getUserId should return empty when no context is set")
    void getUserId_ShouldReturnEmptyWhenNotSet() {
        // When
        Optional<String> result = RequestContextHolder.getUserId();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getCorrelationId should return correlationId when context is set")
    void getCorrelationId_ShouldReturnCorrelationId() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .correlationId(CORRELATION_ID)
                .build();
        RequestContextHolder.set(context);

        // When
        String result = RequestContextHolder.getCorrelationId();

        // Then
        assertThat(result).isEqualTo(CORRELATION_ID);
    }

    @Test
    @DisplayName("getCorrelationId should throw exception when no context is set")
    void getCorrelationId_ShouldThrowExceptionWhenNotSet() {
        // When/Then
        assertThatThrownBy(RequestContextHolder::getCorrelationId)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("RequestContext not set");
    }

    @Test
    @DisplayName("clear should remove context from ThreadLocal")
    void clear_ShouldRemoveContext() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .build();
        RequestContextHolder.set(context);
        assertThat(RequestContextHolder.get()).isPresent();

        // When
        RequestContextHolder.clear();

        // Then
        assertThat(RequestContextHolder.get()).isEmpty();
    }

    @Test
    @DisplayName("Context should be isolated between threads")
    void context_ShouldBeIsolatedBetweenThreads() throws Exception {
        // Given
        String mainTenantId = "main-tenant";
        String threadTenantId = "thread-tenant";

        RequestContext mainContext = RequestContext.builder()
                .tenantId(mainTenantId)
                .build();
        RequestContextHolder.set(mainContext);

        // When - run in separate thread
        Thread thread = new Thread(() -> {
            // In new thread, context should be empty initially
            assertThat(RequestContextHolder.get()).isEmpty();

            // Set new context in thread
            RequestContext threadContext = RequestContext.builder()
                    .tenantId(threadTenantId)
                    .build();
            RequestContextHolder.set(threadContext);

            // Verify thread context
            assertThat(RequestContextHolder.getTenantId()).isEqualTo(threadTenantId);
        });

        thread.start();
        thread.join();

        // Then - main thread context should be unchanged
        assertThat(RequestContextHolder.getTenantId()).isEqualTo(mainTenantId);
    }

    @Test
    @DisplayName("set should overwrite existing context")
    void set_ShouldOverwriteExistingContext() {
        // Given
        RequestContext context1 = RequestContext.builder()
                .tenantId("tenant-1")
                .build();
        RequestContextHolder.set(context1);
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-1");

        // When
        RequestContext context2 = RequestContext.builder()
                .tenantId("tenant-2")
                .build();
        RequestContextHolder.set(context2);

        // Then
        assertThat(RequestContextHolder.getTenantId()).isEqualTo("tenant-2");
    }

    @Test
    @DisplayName("Multiple clear calls should be safe")
    void multipleClearCalls_ShouldBeSafe() {
        // Given
        RequestContextHolder.set(RequestContext.builder().tenantId(TENANT_ID).build());

        // When/Then - should not throw
        RequestContextHolder.clear();
        RequestContextHolder.clear();
        RequestContextHolder.clear();

        assertThat(RequestContextHolder.get()).isEmpty();
    }

    @Test
    @DisplayName("Can set and retrieve context with metadata")
    void canSetAndRetrieveContextWithMetadata() {
        // Given
        Map<String, Object> metadata = Map.of(
                "key1", "value1",
                "key2", 123
        );
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .metadata(metadata)
                .build();

        // When
        RequestContextHolder.set(context);
        RequestContext retrieved = RequestContextHolder.require();

        // Then
        assertThat(retrieved.metadata()).hasSize(2);
        assertThat(retrieved.metadata()).containsEntry("key1", "value1");
        assertThat(retrieved.metadata()).containsEntry("key2", 123);
    }

    @Test
    @DisplayName("Context should persist across multiple calls in same thread")
    void context_ShouldPersistAcrossMultipleCalls() {
        // Given
        RequestContext context = RequestContext.builder()
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .correlationId(CORRELATION_ID)
                .build();
        RequestContextHolder.set(context);

        // When - multiple calls
        String tenantId1 = RequestContextHolder.getTenantId();
        String tenantId2 = RequestContextHolder.getTenantId();
        Optional<String> userId1 = RequestContextHolder.getUserId();
        String correlationId1 = RequestContextHolder.getCorrelationId();

        // Then
        assertThat(tenantId1).isEqualTo(TENANT_ID);
        assertThat(tenantId2).isEqualTo(TENANT_ID);
        assertThat(userId1).isPresent().contains(USER_ID);
        assertThat(correlationId1).isEqualTo(CORRELATION_ID);
    }
}

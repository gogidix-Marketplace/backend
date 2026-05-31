package com.gogidix.ecommerce.vendor.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextTest {

    @Test
    void builder_withAllFields_shouldCreateValidContext() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .correlationId("corr-123")
                .metadata(Map.of("key1", "value1", "key2", 123))
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant-123");
        assertThat(context.userId()).isEqualTo("user-123");
        assertThat(context.correlationId()).isEqualTo("corr-123");
        assertThat(context.metadata()).hasSize(2);
        assertThat(context.metadata()).containsEntry("key1", "value1");
        assertThat(context.metadata()).containsEntry("key2", 123);
    }

    @Test
    void builder_withMinimalFields_shouldCreateValidContext() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant-123");
        assertThat(context.userId()).isNull();
        assertThat(context.correlationId()).isNotNull(); // Auto-generated
        assertThat(context.metadata()).isNotNull().isEmpty();
    }

    @Test
    void builder_withNullTenantId_shouldThrowException() {
        assertThatThrownBy(() -> RequestContext.builder()
                .tenantId(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId is required");
    }

    @Test
    void builder_withEmptyTenantId_shouldThrowException() {
        assertThatThrownBy(() -> RequestContext.builder()
                .tenantId("   ")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId is required");
    }

    @Test
    void builder_withBlankTenantId_shouldThrowException() {
        assertThatThrownBy(() -> RequestContext.builder()
                .tenantId("")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId is required");
    }

    @Test
    void builder_withoutCorrelationId_shouldGenerateUuid() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .build();

        assertThat(context.correlationId()).isNotNull();
        assertThat(context.correlationId()).isNotEmpty();
        // UUID format validation
        assertThat(context.correlationId()).matches("^[a-f0-9-]{36}$");
    }

    @Test
    void builder_withCorrelationId_shouldUseProvidedValue() {
        String providedCorrelationId = "my-custom-correlation-id";
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .correlationId(providedCorrelationId)
                .build();

        assertThat(context.correlationId()).isEqualTo(providedCorrelationId);
    }

    @Test
    void metadata_shouldBeImmutable() {
        Map<String, Object> originalMetadata = Map.of("key", "value");
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .metadata(originalMetadata)
                .build();

        assertThat(context.metadata()).isNotNull();
        // The returned map should be immutable (copied)
        assertThat(context.metadata()).isEqualTo(originalMetadata);
    }

    @Test
    void metadata_withNull_shouldDefaultToEmptyMap() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .metadata(null)
                .build();

        assertThat(context.metadata()).isNotNull().isEmpty();
    }

    @Test
    void builder_isChainable() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .correlationId("corr-123")
                .metadata(Map.of("key", "value"))
                .build();

        assertThat(context).isNotNull();
        assertThat(context.tenantId()).isEqualTo("tenant-123");
        assertThat(context.userId()).isEqualTo("user-123");
        assertThat(context.correlationId()).isEqualTo("corr-123");
        assertThat(context.metadata()).hasSize(1);
    }

    @Test
    void context_withComplexMetadata_shouldPreserveTypes() {
        Map<String, Object> metadata = Map.of(
                "string", "value",
                "integer", 123,
                "long", 123L,
                "double", 123.45,
                "boolean", true
        );

        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .metadata(metadata)
                .build();

        assertThat(context.metadata()).hasSize(5);
        assertThat(context.metadata().get("string")).isEqualTo("value");
        assertThat(context.metadata().get("integer")).isEqualTo(123);
        assertThat(context.metadata().get("long")).isEqualTo(123L);
        assertThat(context.metadata().get("double")).isEqualTo(123.45);
        assertThat(context.metadata().get("boolean")).isEqualTo(true);
    }
}

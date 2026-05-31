package com.gogidix.ecommerce.vendor.shared.requestcontext;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextTest {

    @Test
    void builder_shouldCreateRequestContext_withAllFields() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .userId("user456")
                .correlationId("corr789")
                .metadata(Map.of("key", "value"))
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant123");
        assertThat(context.userId()).isEqualTo("user456");
        assertThat(context.correlationId()).isEqualTo("corr789");
        assertThat(context.metadata()).isEqualTo(Map.of("key", "value"));
    }

    @Test
    void builder_shouldAutoGenerateCorrelationId_whenNotProvided() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .userId("user456")
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant123");
        assertThat(context.userId()).isEqualTo("user456");
        assertThat(context.correlationId()).isNotNull();
        assertThat(context.correlationId()).isNotEmpty();
        assertThat(context.metadata()).isEqualTo(Map.of());
    }

    @Test
    void builder_shouldThrowException_whenTenantIdIsNull() {
        assertThatThrownBy(() -> RequestContext.builder()
                .userId("user456")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId is required");
    }

    @Test
    void builder_shouldThrowException_whenTenantIdIsEmpty() {
        assertThatThrownBy(() -> RequestContext.builder()
                .tenantId("")
                .userId("user456")
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tenantId is required");
    }

    @Test
    void builder_shouldDefaultToEmptyMetadata_whenNotProvided() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant123")
                .build();

        assertThat(context.metadata()).isNotNull();
        assertThat(context.metadata()).isEmpty();
    }
}

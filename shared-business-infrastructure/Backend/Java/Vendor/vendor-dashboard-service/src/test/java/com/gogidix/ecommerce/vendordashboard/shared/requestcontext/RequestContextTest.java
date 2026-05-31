package com.gogidix.ecommerce.vendor.shared.requestcontext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestContextTest {

    @Test
    void builder_withAllFields_shouldCreateValidContext() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .userId("user-123")
                .correlationId("corr-123")
                .metadata(java.util.Map.of("key1", "value1"))
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant-123");
        assertThat(context.userId()).isEqualTo("user-123");
        assertThat(context.correlationId()).isEqualTo("corr-123");
        assertThat(context.metadata()).hasSize(1);
    }

    @Test
    void builder_withMinimalFields_shouldCreateValidContext() {
        RequestContext context = RequestContext.builder()
                .tenantId("tenant-123")
                .build();

        assertThat(context.tenantId()).isEqualTo("tenant-123");
        assertThat(context.userId()).isNull();
        assertThat(context.correlationId()).isNotNull();
        assertThat(context.metadata()).isNotNull().isEmpty();
    }

    @Test
    void builder_withNullTenantId_shouldThrowException() {
        assertThatThrownBy(() -> RequestContext.builder()
                .tenantId(null)
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}

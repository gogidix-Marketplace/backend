package com.gogidix.aiservices.aiproductrecommendationservice.shared.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("RequestContextHolder Tests")
class RequestContextHolderTest {

    @AfterEach
    void cleanup() {
        RequestContextHolder.clear();
    }

    @Nested
    @DisplayName("Context Management Tests")
    class ContextManagementTests {
        @Test
        void shouldSetAndGetContext() {
            RequestContext context = RequestContext.create("tenant-1", "user-1");
            RequestContextHolder.setContext(context);
            assertThat(RequestContextHolder.getContext()).isEqualTo(context);
        }
        @Test
        void shouldThrowWhenNotSet() {
            assertThatThrownBy(() -> RequestContextHolder.getContext())
                    .isInstanceOf(IllegalStateException.class);
        }
        @Test
        void shouldReturnEmptyOptionalWhenNotSet() {
            assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
        }
        @Test
        void shouldClearContext() {
            RequestContextHolder.setContext(RequestContext.create("t", "u"));
            RequestContextHolder.clear();
            assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Convenience Method Tests")
    class ConvenienceMethodTests {
        @Test
        void shouldGetTenantId() {
            RequestContextHolder.setContext(RequestContext.create("my-tenant", "my-user"));
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("my-tenant");
        }
        @Test
        void shouldGetUserId() {
            RequestContextHolder.setContext(RequestContext.create("t", "my-user"));
            assertThat(RequestContextHolder.getUserId()).isEqualTo("my-user");
        }
        @Test
        void shouldGetCorrelationId() {
            RequestContextHolder.setContext(RequestContext.createWithCorrelationId("t", "u", "corr-123"));
            assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr-123");
        }
    }

    @Nested
    @DisplayName("RequestContext Record Tests")
    class RequestContextRecordTests {
        @Test
        void shouldCreateWithAutoFields() {
            RequestContext ctx = RequestContext.create("tenant-1", "user-1");
            assertThat(ctx.tenantId()).isEqualTo("tenant-1");
            assertThat(ctx.correlationId()).isNotNull();
        }
        @Test
        void shouldCreateWithCorrelationId() {
            RequestContext ctx = RequestContext.createWithCorrelationId("t", "u", "my-corr");
            assertThat(ctx.correlationId()).isEqualTo("my-corr");
        }
        @Test
        void shouldGenerateWhenNullCorrelationId() {
            RequestContext ctx = RequestContext.createWithCorrelationId("t", "u", null);
            assertThat(ctx.correlationId()).isNotNull();
        }
        @Test
        void shouldCreateAnonymous() {
            RequestContext ctx = RequestContext.anonymous();
            assertThat(ctx.tenantId()).isEqualTo("system");
        }
    }
}

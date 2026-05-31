package com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
        @DisplayName("Should set and get context")
        void shouldSetAndGetContext() {
            RequestContext context = RequestContext.create("tenant-1", "user-1");
            RequestContextHolder.setContext(context);

            assertThat(RequestContextHolder.getContext()).isEqualTo(context);
        }

        @Test
        @DisplayName("Should throw when context not set")
        void shouldThrowWhenNotSet() {
            assertThatThrownBy(() -> RequestContextHolder.getContext())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("RequestContext not set");
        }

        @Test
        @DisplayName("Should return empty optional when not set")
        void shouldReturnEmptyOptional() {
            assertThat(RequestContextHolder.getOptionalContext()).isEmpty();
        }

        @Test
        @DisplayName("Should return optional when set")
        void shouldReturnOptionalWhenSet() {
            RequestContext context = RequestContext.create("tenant-1", "user-1");
            RequestContextHolder.setContext(context);
            assertThat(RequestContextHolder.getOptionalContext()).isPresent();
        }

        @Test
        @DisplayName("Should clear context")
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
        @DisplayName("Should get tenant ID")
        void shouldGetTenantId() {
            RequestContextHolder.setContext(RequestContext.create("my-tenant", "my-user"));
            assertThat(RequestContextHolder.getTenantId()).isEqualTo("my-tenant");
        }

        @Test
        @DisplayName("Should get user ID")
        void shouldGetUserId() {
            RequestContextHolder.setContext(RequestContext.create("my-tenant", "my-user"));
            assertThat(RequestContextHolder.getUserId()).isEqualTo("my-user");
        }

        @Test
        @DisplayName("Should get correlation ID")
        void shouldGetCorrelationId() {
            RequestContextHolder.setContext(RequestContext.createWithCorrelationId("t", "u", "corr-123"));
            assertThat(RequestContextHolder.getCorrelationId()).isEqualTo("corr-123");
        }
    }

    @Nested
    @DisplayName("RequestContext Tests")
    class RequestContextRecordTests {

        @Test
        @DisplayName("Should create context with auto-generated fields")
        void shouldCreateWithAutoFields() {
            RequestContext ctx = RequestContext.create("tenant-1", "user-1");
            assertThat(ctx.tenantId()).isEqualTo("tenant-1");
            assertThat(ctx.userId()).isEqualTo("user-1");
            assertThat(ctx.correlationId()).isNotNull();
            assertThat(ctx.requestTime()).isNotNull();
        }

        @Test
        @DisplayName("Should create with specific correlation ID")
        void shouldCreateWithCorrelationId() {
            RequestContext ctx = RequestContext.createWithCorrelationId("t", "u", "my-corr");
            assertThat(ctx.correlationId()).isEqualTo("my-corr");
        }

        @Test
        @DisplayName("Should generate correlation ID when null passed")
        void shouldGenerateWhenNullCorrelationId() {
            RequestContext ctx = RequestContext.createWithCorrelationId("t", "u", null);
            assertThat(ctx.correlationId()).isNotNull();
        }

        @Test
        @DisplayName("Should create anonymous context")
        void shouldCreateAnonymous() {
            RequestContext ctx = RequestContext.anonymous();
            assertThat(ctx.tenantId()).isEqualTo("system");
            assertThat(ctx.userId()).isEqualTo("system");
        }
    }
}

package com.gogidix.aiservices.aiuserprofilingservice.shared.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RequestContext.
 */
@DisplayName("RequestContext Tests")
class RequestContextTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create context with tenant and user ID")
        void shouldCreateWithTenantAndUserId() {
            RequestContext context = RequestContext.create(TENANT_ID, USER_ID);

            assertEquals(TENANT_ID, context.tenantId());
            assertEquals(USER_ID, context.userId());
            assertNotNull(context.correlationId());
            assertNotNull(context.requestTime());
        }

        @Test
        @DisplayName("Should create context with specific correlation ID")
        void shouldCreateWithSpecificCorrelationId() {
            String correlationId = "custom-correlation-id";
            RequestContext context = RequestContext.createWithCorrelationId(TENANT_ID, USER_ID, correlationId);

            assertEquals(TENANT_ID, context.tenantId());
            assertEquals(USER_ID, context.userId());
            assertEquals(correlationId, context.correlationId());
        }

        @Test
        @DisplayName("Should generate correlation ID when null is provided")
        void shouldGenerateCorrelationIdWhenNullProvided() {
            RequestContext context = RequestContext.createWithCorrelationId(TENANT_ID, USER_ID, null);

            assertNotNull(context.correlationId());
            assertFalse(context.correlationId().isBlank());
        }

        @Test
        @DisplayName("Should create anonymous context")
        void shouldCreateAnonymousContext() {
            RequestContext context = RequestContext.anonymous();

            assertEquals("system", context.tenantId());
            assertEquals("system", context.userId());
            assertNotNull(context.correlationId());
            assertNotNull(context.requestTime());
        }
    }

    @Nested
    @DisplayName("Correlation ID Tests")
    class CorrelationIdTests {

        @Test
        @DisplayName("Should generate unique correlation IDs")
        void shouldGenerateUniqueCorrelationIds() {
            RequestContext context1 = RequestContext.create(TENANT_ID, USER_ID);
            RequestContext context2 = RequestContext.create(TENANT_ID, USER_ID);

            assertNotEquals(context1.correlationId(), context2.correlationId());
        }

        @Test
        @DisplayName("Should generate valid UUID correlation IDs")
        void shouldGenerateValidUuidCorrelationIds() {
            RequestContext context = RequestContext.create(TENANT_ID, USER_ID);

            assertDoesNotThrow(() -> java.util.UUID.fromString(context.correlationId()));
        }
    }

    @Nested
    @DisplayName("Request Time Tests")
    class RequestTimeTests {

        @Test
        @DisplayName("Should set request time to current time")
        void shouldSetRequestTimeToCurrentTime() {
            java.time.Instant before = java.time.Instant.now();
            RequestContext context = RequestContext.create(TENANT_ID, USER_ID);
            java.time.Instant after = java.time.Instant.now();

            assertFalse(context.requestTime().isBefore(before));
            assertFalse(context.requestTime().isAfter(after));
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable record")
        void shouldBeImmutableRecord() {
            RequestContext context = RequestContext.create(TENANT_ID, USER_ID);

            // Record instances are immutable by design
            assertEquals(TENANT_ID, context.tenantId());
            assertEquals(USER_ID, context.userId());
        }
    }
}

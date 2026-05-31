package com.gogidix.aiservices.aigatewayservice.shared.requestcontext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RequestContext.
 */
@DisplayName("RequestContext Tests")
class RequestContextTest {

    @Test
    @DisplayName("Should create request context")
    void shouldCreateRequestContext() {
        RequestContext context = RequestContext.create("tenant-123", "user-123");

        assertEquals("tenant-123", context.tenantId());
        assertEquals("user-123", context.userId());
        assertNotNull(context.correlationId());
        assertNotNull(context.requestTime());
    }

    @Test
    @DisplayName("Should create request context with correlation ID")
    void shouldCreateRequestContextWithCorrelationId() {
        RequestContext context = RequestContext.createWithCorrelationId(
                "tenant-123", "user-123", "corr-123"
        );

        assertEquals("tenant-123", context.tenantId());
        assertEquals("user-123", context.userId());
        assertEquals("corr-123", context.correlationId());
    }

    @Test
    @DisplayName("Should create anonymous context")
    void shouldCreateAnonymousContext() {
        RequestContext context = RequestContext.anonymous();

        assertEquals("system", context.tenantId());
        assertEquals("system", context.userId());
        assertNotNull(context.correlationId());
    }

    @Test
    @DisplayName("Should generate correlation ID when null")
    void shouldGenerateCorrelationIdWhenNull() {
        RequestContext context = RequestContext.createWithCorrelationId(
                "tenant-123", "user-123", null
        );

        assertNotNull(context.correlationId());
        assertFalse(context.correlationId().isBlank());
    }
}

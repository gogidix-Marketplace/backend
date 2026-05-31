package com.gogidix.aiservices.intelligenceanalysisservice.shared.context;

import java.time.Instant;
import java.util.UUID;

/**
 * Request context holding tenant and user information for the current request.
 */
public record RequestContext(

        String tenantId,

        String userId,

        String correlationId,

        Instant requestTime

) {

    /**
     * Create a new request context.
     */
    public static RequestContext create(String tenantId, String userId) {
        return new RequestContext(
                tenantId,
                userId,
                UUID.randomUUID().toString(),
                Instant.now()
        );
    }

    /**
     * Create a request context with a specific correlation ID.
     */
    public static RequestContext createWithCorrelationId(String tenantId, String userId, String correlationId) {
        return new RequestContext(
                tenantId,
                userId,
                correlationId != null ? correlationId : UUID.randomUUID().toString(),
                Instant.now()
        );
    }

    /**
     * Create an anonymous context for testing or internal operations.
     */
    public static RequestContext anonymous() {
        return new RequestContext(
                "system",
                "system",
                UUID.randomUUID().toString(),
                Instant.now()
        );
    }
}

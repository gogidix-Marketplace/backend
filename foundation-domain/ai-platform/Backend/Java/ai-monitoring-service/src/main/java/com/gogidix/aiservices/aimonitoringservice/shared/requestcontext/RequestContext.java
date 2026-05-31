package com.gogidix.aiservices.aimonitoringservice.shared.requestcontext;

import java.time.Instant;
import java.util.UUID;

/**
 * Request context holding tenant and user information.
 */
public record RequestContext(
        String tenantId,
        String userId,
        String correlationId,
        Instant requestTime
) {
    public static RequestContext create(String tenantId, String userId) {
        return new RequestContext(tenantId, userId, UUID.randomUUID().toString(), Instant.now());
    }

    public static RequestContext anonymous() {
        return new RequestContext("system", "system", UUID.randomUUID().toString(), Instant.now());
    }
}

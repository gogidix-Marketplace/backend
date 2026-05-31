package com.gogidix.courier.availabilityservice.shared.context;

import java.time.Instant;
import java.util.Objects;

/**
 * Request context holding tenant and user information.
 */
public record RequestContext(
        String tenantId,
        String userId,
        String correlationId,
        Instant timestamp
) {
    public static RequestContext of(String tenantId, String userId, String correlationId) {
        return new RequestContext(
                Objects.requireNonNullElse(tenantId, ""),
                Objects.requireNonNullElse(userId, ""),
                Objects.requireNonNullElse(correlationId, ""),
                Instant.now()
        );
    }

    public static RequestContext empty() {
        return new RequestContext("", "", "", Instant.now());
    }
}

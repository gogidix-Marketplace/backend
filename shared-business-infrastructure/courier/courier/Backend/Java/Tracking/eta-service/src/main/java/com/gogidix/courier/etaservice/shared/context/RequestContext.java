package com.gogidix.courier.etaservice.shared.context;

import java.time.Instant;
import java.util.Optional;

/**
 * Request context containing tenant, user, and correlation information.
 */
public record RequestContext(
        String tenantId,
        String userId,
        String correlationId,
        Instant timestamp
) {

    private static final String ANONYMOUS_TENANT = "system";
    private static final String ANONYMOUS_USER = "system";

    /**
     * Create an anonymous request context.
     */
    public static RequestContext anonymous() {
        return new RequestContext(
                ANONYMOUS_TENANT,
                ANONYMOUS_USER,
                java.util.UUID.randomUUID().toString(),
                Instant.now()
        );
    }

    /**
     * Create a request context with the given values.
     */
    public static RequestContext of(String tenantId, String userId, String correlationId) {
        return new RequestContext(
                tenantId != null && !tenantId.isBlank() ? tenantId : ANONYMOUS_TENANT,
                userId != null && !userId.isBlank() ? userId : ANONYMOUS_USER,
                correlationId != null && !correlationId.isBlank() ? correlationId : java.util.UUID.randomUUID().toString(),
                Instant.now()
        );
    }

    /**
     * Create a builder for this context.
     */
    public static Builder builder() {
        return new Builder();
    }

    public Optional<String> getTenantIdOptional() {
        return Optional.ofNullable(tenantId).filter(s -> !s.equals(ANONYMOUS_TENANT));
    }

    public Optional<String> getUserIdOptional() {
        return Optional.ofNullable(userId).filter(s -> !s.equals(ANONYMOUS_USER));
    }

    /**
     * Builder for RequestContext.
     */
    public static class Builder {
        private String tenantId = ANONYMOUS_TENANT;
        private String userId = ANONYMOUS_USER;
        private String correlationId;
        private Instant timestamp = Instant.now();

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public RequestContext build() {
            return new RequestContext(
                    tenantId != null && !tenantId.isBlank() ? tenantId : ANONYMOUS_TENANT,
                    userId != null && !userId.isBlank() ? userId : ANONYMOUS_USER,
                    correlationId != null && !correlationId.isBlank() ? correlationId : java.util.UUID.randomUUID().toString(),
                    timestamp != null ? timestamp : Instant.now()
            );
        }
    }
}

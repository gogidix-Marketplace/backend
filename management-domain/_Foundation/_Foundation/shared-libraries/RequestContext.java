package com.gogidix.management.shared.requestcontext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * RequestContext - ThreadLocal holder for tenant context
 *
 * <p>MUST be populated by TenantInterceptor BEFORE any service logic.
 * This class is immutable and thread-safe.</p>
 *
 * <p>Usage:</p>
 * <pre>
 * RequestContext context = RequestContext.builder()
 *     .tenantId("tenant-123")
 *     .userId("user-456")
 *     .correlationId("corr-789")
 *     .build();
 *
 * RequestContextHolder.set(context);
 * </pre>
 *
 * @see RequestContextHolder
 * @see com.gogidix.management.infrastructure.security.TenantInterceptor
 */
public final class RequestContext {

    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final Map<String, Object> metadata;
    private final String region;
    private final String country;
    private final String traceId;

    private RequestContext(Builder builder) {
        this.tenantId = Objects.requireNonNull(builder.tenantId, "tenantId is required");
        this.userId = builder.userId;
        this.correlationId = Objects.requireNonNull(builder.correlationId, "correlationId is required");
        this.metadata = builder.metadata != null ? Map.copyOf(builder.metadata) : Map.of();
        this.region = builder.region;
        this.country = builder.country;
        this.traceId = builder.traceId;
    }

    /**
     * Get the tenant ID for this request.
     * All database queries MUST filter by this tenantId.
     *
     * @return the tenant ID, never null
     */
    public String tenantId() {
        return tenantId;
    }

    /**
     * Get the user ID for this request.
     *
     * @return the user ID, or null if not authenticated
     */
    public String userId() {
        return userId;
    }

    /**
     * Get the correlation ID for tracing this request.
     *
     * @return the correlation ID, never null
     */
    public String correlationId() {
        return correlationId;
    }

    /**
     * Get additional metadata for this request.
     *
     * @return immutable metadata map, never null
     */
    public Map<String, Object> metadata() {
        return metadata;
    }

    /**
     * Get the region code (e.g., "EUROPE", "AFRICA").
     *
     * @return the region code, or null if not set
     */
    public String region() {
        return region;
    }

    /**
     * Get the country code (e.g., "IE", "NG", "KE").
     *
     * @return the country code, or null if not set
     */
    public String country() {
        return country;
    }

    /**
     * Get the distributed trace ID.
     *
     * @return the trace ID, or null if not set
     */
    public String traceId() {
        return traceId;
    }

    /**
     * Create a new builder for RequestContext.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create a new builder initialized with values from an existing RequestContext.
     *
     * @param existing the existing context to copy from
     * @return a new Builder instance with pre-populated values
     */
    public static Builder builder(RequestContext existing) {
        return new Builder()
            .tenantId(existing.tenantId)
            .userId(existing.userId)
            .correlationId(existing.correlationId)
            .region(existing.region)
            .country(existing.country)
            .traceId(existing.traceId)
            .metadata(existing.metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestContext that = (RequestContext) o;
        return Objects.equals(tenantId, that.tenantId)
            && Objects.equals(userId, that.userId)
            && Objects.equals(correlationId, that.correlationId)
            && Objects.equals(region, that.region)
            && Objects.equals(country, that.country)
            && Objects.equals(traceId, that.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, userId, correlationId, region, country, traceId);
    }

    @Override
    public String toString() {
        return "RequestContext{" +
            "tenantId='" + tenantId + '\'' +
            ", userId='" + userId + '\'' +
            ", correlationId='" + correlationId + '\'' +
            ", region='" + region + '\'' +
            ", country='" + country + '\'' +
            ", traceId='" + traceId + '\'' +
            '}';
    }

    /**
     * Builder for creating RequestContext instances.
     */
    public static final class Builder {
        private String tenantId;
        private String userId;
        private String correlationId;
        private Map<String, Object> metadata;
        private String region;
        private String country;
        private String traceId;

        private Builder() {}

        /**
         * Set the tenant ID (required).
         *
         * @param tenantId the tenant ID
         * @return this builder
         */
        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        /**
         * Set the user ID.
         *
         * @param userId the user ID
         * @return this builder
         */
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Set the correlation ID (required).
         *
         * @param correlationId the correlation ID
         * @return this builder
         */
        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        /**
         * Set additional metadata.
         *
         * @param metadata the metadata map
         * @return this builder
         */
        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        /**
         * Add a single metadata entry.
         *
         * @param key the metadata key
         * @param value the metadata value
         * @return this builder
         */
        public Builder addMetadata(String key, Object value) {
            if (this.metadata == null) {
                this.metadata = Map.of();
            }
            this.metadata = Map.copyOf(new java.util.HashMap<>(this.metadata) {{
                put(key, value);
            }});
            return this;
        }

        /**
         * Set the region code.
         *
         * @param region the region code
         * @return this builder
         */
        public Builder region(String region) {
            this.region = region;
            return this;
        }

        /**
         * Set the country code.
         *
         * @param country the country code
         * @return this builder
         */
        public Builder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * Set the distributed trace ID.
         *
         * @param traceId the trace ID
         * @return this builder
         */
        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        /**
         * Build the RequestContext.
         *
         * @return a new RequestContext instance
         * @throws IllegalArgumentException if tenantId or correlationId is null
         */
        public RequestContext build() {
            return new RequestContext(this);
        }
    }
}

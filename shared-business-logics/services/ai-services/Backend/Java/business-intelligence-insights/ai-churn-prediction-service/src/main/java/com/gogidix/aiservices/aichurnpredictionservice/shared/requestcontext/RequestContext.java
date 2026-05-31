package com.gogidix.aiservices.aichurnpredictionservice.shared.requestcontext;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * ThreadLocal holder for tenant context throughout request lifecycle.
 * MUST be populated by TenantInterceptor BEFORE any service logic.
 * This ensures multi-tenant isolation at all layers.
 */
public final class RequestContext {

    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final Map<String, Object> metadata;

    private RequestContext(Builder builder) {
        this.tenantId = Objects.requireNonNull(builder.tenantId, "tenantId is required");
        this.userId = builder.userId;
        this.correlationId = Objects.requireNonNull(builder.correlationId, "correlationId is required");
        this.metadata = builder.metadata != null ? Map.copyOf(builder.metadata) : Map.of();
    }

    public String tenantId() {
        return tenantId;
    }

    public String userId() {
        return userId;
    }

    public String correlationId() {
        return correlationId;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public Optional<String> getMetadataValue(String key) {
        return Optional.ofNullable(metadata.get(key)).map(Object::toString);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(RequestContext existing) {
        return new Builder()
                .tenantId(existing.tenantId)
                .userId(existing.userId)
                .correlationId(existing.correlationId)
                .metadata(existing.metadata);
    }

    public static class Builder {
        private String tenantId;
        private String userId;
        private String correlationId;
        private Map<String, Object> metadata;

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

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            if (this.metadata == null) {
                this.metadata = Map.of();
            }
            this.metadata = Map.copyOf(new java.util.HashMap<>(this.metadata) {{
                put(key, value);
            }});
            return this;
        }

        public RequestContext build() {
            return new RequestContext(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestContext that = (RequestContext) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(correlationId, that.correlationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, userId, correlationId);
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "tenantId='" + tenantId + '\'' +
                ", userId='" + userId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}

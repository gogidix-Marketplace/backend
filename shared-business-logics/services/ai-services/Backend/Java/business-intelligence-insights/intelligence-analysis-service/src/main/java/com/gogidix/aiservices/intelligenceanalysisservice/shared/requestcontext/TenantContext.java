package com.gogidix.aiservices.intelligenceanalysisservice.shared.requestcontext;

import java.util.Set;
import java.util.HashSet;

/**
 * Tenant context for ai-customer-segmentation-service.
 * Holds tenant-specific information throughout request processing.
 */
public class TenantContext {
    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final Set<String> roles;

    private TenantContext(Builder builder) {
        this.tenantId = builder.tenantId;
        this.userId = builder.userId;
        this.correlationId = builder.correlationId;
        this.roles = builder.roles;
    }

    public String getTenantId() { return tenantId; }
    public String getUserId() { return userId; }
    public String getCorrelationId() { return correlationId; }
    public Set<String> getRoles() { return roles; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String userId;
        private String correlationId;
        private Set<String> roles = new HashSet<>();

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

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public TenantContext build() {
            return new TenantContext(this);
        }
    }
}

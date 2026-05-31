package com.gogidix.ecommerce.pushnotification.shared.requestcontext;

import java.util.Map;

public record RequestContext(
    String tenantId,
    String customerId,
    String userId,
    String correlationId,
    Map<String, String> metadata
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String tenantId;
        private String customerId;
        private String userId;
        private String correlationId;
        private Map<String, String> metadata;

        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder metadata(Map<String, String> metadata) { this.metadata = metadata; return this; }

        public RequestContext build() {
            if (tenantId == null) throw new IllegalArgumentException("tenantId is required");
            return new RequestContext(tenantId, customerId, userId,
                correlationId != null ? correlationId : java.util.UUID.randomUUID().toString(),
                metadata != null ? metadata : Map.of());
        }
    }
}

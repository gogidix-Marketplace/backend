package com.gogidix.management.executive.operations.shared.requestcontext;

import java.util.Map;

public final class RequestContext {
    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final Map<String, Object> metadata;
    private RequestContext(Builder b) { this.tenantId = b.tenantId; this.userId = b.userId; this.correlationId = b.correlationId != null ? b.correlationId : java.util.UUID.randomUUID().toString(); this.metadata = b.metadata != null ? Map.copyOf(b.metadata) : Map.of(); }
    public String tenantId() { return tenantId; }
    public String userId() { return userId; }
    public String correlationId() { return correlationId; }
    public Map<String, Object> metadata() { return metadata; }
    public static Builder builder() { return new Builder(); }
    public static class Builder { private String tenantId; private String userId; private String correlationId; private Map<String, Object> metadata;
        public Builder tenantId(String t) { this.tenantId = t; return this; }
        public Builder userId(String u) { this.userId = u; return this; }
        public Builder correlationId(String c) { this.correlationId = c; return this; }
        public Builder metadata(Map<String, Object> m) { this.metadata = m; return this; }
        public RequestContext build() { if (tenantId == null || tenantId.isBlank()) throw new IllegalArgumentException("tenantId is required"); return new RequestContext(this); }
    }
}

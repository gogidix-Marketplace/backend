package com.gogidix.hr.payroll.shared.requestcontext;

import java.util.Map;

public final class RequestContext {
    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final String ipAddress;
    private final String userAgent;
    private final String countryCode;
    private final Map<String, Object> metadata;

    private RequestContext(Builder builder) {
        this.tenantId = builder.tenantId;
        this.userId = builder.userId;
        this.correlationId = builder.correlationId != null ? builder.correlationId : java.util.UUID.randomUUID().toString();
        this.ipAddress = builder.ipAddress;
        this.userAgent = builder.userAgent;
        this.countryCode = builder.countryCode;
        this.metadata = builder.metadata != null ? Map.copyOf(builder.metadata) : Map.of();
    }

    public String tenantId() { return tenantId; }
    public String userId() { return userId; }
    public String correlationId() { return correlationId; }
    public String getCorrelationId() { return correlationId; }
    public String ipAddress() { return ipAddress; }
    public String userAgent() { return userAgent; }
    public String countryCode() { return countryCode; }
    public Map<String, Object> metadata() { return metadata; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String tenantId;
        private String userId;
        private String correlationId;
        private String ipAddress;
        private String userAgent;
        private String countryCode;
        private Map<String, Object> metadata;

        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }

        public RequestContext build() {
            if (tenantId == null || tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            return new RequestContext(this);
        }
    }
}

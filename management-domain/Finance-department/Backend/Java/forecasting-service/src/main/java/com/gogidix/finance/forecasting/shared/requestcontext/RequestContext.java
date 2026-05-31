package com.gogidix.finance.forecasting.shared.requestcontext;

import java.util.Map;

/**
 * Request Context
 * Holds tenant and user information for the current request
 * Used for multi-tenant isolation throughout the application
 */
public final class RequestContext {

    private final String tenantId;
    private final String userId;
    private final String correlationId;
    private final String requestId;
    private final String userAgent;
    private final String ipAddress;
    private final String requestMethod;
    private final String requestUri;
    private final Map<String, Object> metadata;

    private RequestContext(Builder builder) {
        this.tenantId = builder.tenantId;
        this.userId = builder.userId;
        this.correlationId = builder.correlationId != null
                ? builder.correlationId
                : java.util.UUID.randomUUID().toString();
        this.requestId = builder.requestId != null
                ? builder.requestId
                : java.util.UUID.randomUUID().toString();
        this.userAgent = builder.userAgent;
        this.ipAddress = builder.ipAddress;
        this.requestMethod = builder.requestMethod;
        this.requestUri = builder.requestUri;
        this.metadata = builder.metadata != null
                ? Map.copyOf(builder.metadata)
                : Map.of();
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

    public String getCorrelationId() {
        return correlationId;
    }

    public String requestId() {
        return requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public Map<String, Object> metadata() {
        return metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String userId;
        private String correlationId;
        private String requestId;
        private String userAgent;
        private String ipAddress;
        private String requestMethod;
        private String requestUri;
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

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder requestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder requestUri(String requestUri) {
            this.requestUri = requestUri;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public RequestContext build() {
            if (tenantId == null || tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
            return new RequestContext(this);
        }
    }
}

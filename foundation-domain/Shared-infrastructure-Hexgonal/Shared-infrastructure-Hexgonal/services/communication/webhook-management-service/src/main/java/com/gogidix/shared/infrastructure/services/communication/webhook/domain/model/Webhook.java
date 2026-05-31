package com.gogidix.shared.infrastructure.services.communication.webhook.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Webhook entity with multi-tenant support.
 */
@Document(collection = "webhooks")
public class Webhook {
    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed
    private String name;

    private String description;

    @Indexed
    private String url;

    @Indexed
    private String status; // ACTIVE, INACTIVE, DISABLED

    @Indexed
    private String eventType; // The event type that triggers this webhook

    private List<String> eventTypes = new ArrayList<>();

    private String httpMethod; // GET, POST, PUT, PATCH

    private List<WebhookHeader> headers = new ArrayList<>();

    private String secret; // For HMAC signature verification

    @Indexed
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Integer retryAttempts;

    private Long retryDelay; // in milliseconds

    private Integer timeout; // in seconds

    @Indexed
    private Boolean sslVerificationEnabled;

    public Webhook() {
        // MongoDB
    }

    public Webhook(TenantId tenantId, String name, String url, String eventType) {
        this.tenantId = tenantId;
        this.name = name;
        this.url = url;
        this.eventType = eventType;
        this.status = "ACTIVE";
        this.httpMethod = "POST";
        this.retryAttempts = 3;
        this.retryDelay = 1000L;
        this.timeout = 30;
        this.sslVerificationEnabled = true;
    }

    // Getters and setters
    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<WebhookHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<WebhookHeader> headers) {
        this.headers = headers;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public Long getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(Long retryDelay) {
        this.retryDelay = retryDelay;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getSslVerificationEnabled() {
        return sslVerificationEnabled;
    }

    public void setSslVerificationEnabled(Boolean sslVerificationEnabled) {
        this.sslVerificationEnabled = sslVerificationEnabled;
    }

    /**
     * Nested class for webhook headers
     */
    public static class WebhookHeader {
        private String key;
        private String value;

        public WebhookHeader() {
        }

        public WebhookHeader(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

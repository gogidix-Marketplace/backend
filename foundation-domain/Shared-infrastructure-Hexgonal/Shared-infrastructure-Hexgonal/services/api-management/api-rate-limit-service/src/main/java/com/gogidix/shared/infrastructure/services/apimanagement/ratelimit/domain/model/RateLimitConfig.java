package com.gogidix.shared.infrastructure.services.apimanagement.ratelimit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "rate_limit_configs")
public class RateLimitConfig {

    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed
    private String apiKey;

    @Indexed
    private String endpoint;

    @Indexed
    private int requestsPerMinute;

    @Indexed
    private int requestsPerHour;

    @Indexed
    private int requestsPerDay;

    @Indexed
    private boolean active;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public RateLimitConfig() {
    }

    public RateLimitConfig(TenantId tenantId, String apiKey, String endpoint) {
        this.tenantId = tenantId;
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.active = true;
        this.requestsPerMinute = 60;
        this.requestsPerHour = 1000;
        this.requestsPerDay = 10000;
    }

    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public int getRequestsPerMinute() { return requestsPerMinute; }
    public void setRequestsPerMinute(int requestsPerMinute) { this.requestsPerMinute = requestsPerMinute; }
    public int getRequestsPerHour() { return requestsPerHour; }
    public void setRequestsPerHour(int requestsPerHour) { this.requestsPerHour = requestsPerHour; }
    public int getRequestsPerDay() { return requestsPerDay; }
    public void setRequestsPerDay(int requestsPerDay) { this.requestsPerDay = requestsPerDay; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;
/**
 * MongoDB Document: EventBridge
 * Represents an event bus bridge configuration with multi-tenant support.
 */
@Document(collection = "event_bridges")
public class EventBridge {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String sourceType;
    @Indexed
    private String targetType;
    private String sourceTopic;
    private String targetTopic;
    private String sourceExchange;
    private String targetExchange;
    private String sourceQueue;
    private String targetQueue;
    private boolean enabled;
    private String filterExpression;
    private Map<String, Object> transformationRules;
    private int retryCount;
    private int maxRetries;
    private long messageCount;
    private long errorCount;
    private String status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    public EventBridge() {
        // MongoDB
    }
    public EventBridge(TenantId tenantId, String name, String sourceType, String targetType) {
        this.tenantId = tenantId;
        this.name = name;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.enabled = true;
        this.retryCount = 0;
        this.maxRetries = 3;
        this.messageCount = 0;
        this.errorCount = 0;
        this.status = "ACTIVE";
    }
    // Getters
    public TenantId getTenantId() { return tenantId; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSourceType() { return sourceType; }
    public String getTargetType() { return targetType; }
    public String getSourceTopic() { return sourceTopic; }
    public String getTargetTopic() { return targetTopic; }
    public String getSourceExchange() { return sourceExchange; }
    public String getTargetExchange() { return targetExchange; }
    public String getSourceQueue() { return sourceQueue; }
    public String getTargetQueue() { return targetQueue; }
    public boolean isEnabled() { return enabled; }
    public String getFilterExpression() { return filterExpression; }
    public Map<String, Object> getTransformationRules() { return transformationRules; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public long getMessageCount() { return messageCount; }
    public long getErrorCount() { return errorCount; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    // Setters
    public void setId(String id) { this.id = id; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public void setName(String name) { this.name = name; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public void setSourceTopic(String sourceTopic) { this.sourceTopic = sourceTopic; }
    public void setTargetTopic(String targetTopic) { this.targetTopic = targetTopic; }
    public void setSourceExchange(String sourceExchange) { this.sourceExchange = sourceExchange; }
    public void setTargetExchange(String targetExchange) { this.targetExchange = targetExchange; }
    public void setSourceQueue(String sourceQueue) { this.sourceQueue = sourceQueue; }
    public void setTargetQueue(String targetQueue) { this.targetQueue = targetQueue; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setFilterExpression(String filterExpression) { this.filterExpression = filterExpression; }
    public void setTransformationRules(Map<String, Object> transformationRules) { this.transformationRules = transformationRules; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void setMessageCount(long messageCount) { this.messageCount = messageCount; }
    public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
    public void setStatus(String status) { this.status = status; }
}

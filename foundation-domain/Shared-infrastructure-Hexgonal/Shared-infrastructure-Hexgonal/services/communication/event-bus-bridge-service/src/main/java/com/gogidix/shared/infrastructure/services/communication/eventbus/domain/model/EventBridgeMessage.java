package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
/**
 * MongoDB Document: EventBridgeMessage
 * Represents a message that has been bridged between event buses with multi-tenant support.
 */
@Document(collection = "event_bridge_messages")
public class EventBridgeMessage {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String bridgeId;
    @Indexed
    private String correlationId;
    private String sourceType;
    private String targetType;
    private String sourceTopic;
    private String targetTopic;
    private String payload;
    private String headers;
    @Indexed
    private String status;
    private String errorMessage;
    private int retryCount;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    public EventBridgeMessage() {
        // MongoDB
    }
    public EventBridgeMessage(TenantId tenantId, String bridgeId, String correlationId,
                             String sourceType, String targetType, String payload) {
        this.tenantId = tenantId;
        this.bridgeId = bridgeId;
        this.correlationId = correlationId;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.payload = payload;
        this.status = "PENDING";
        this.retryCount = 0;
    }
    // Getters
    public TenantId getTenantId() { return tenantId; }
    public String getId() { return id; }
    public String getBridgeId() { return bridgeId; }
    public String getCorrelationId() { return correlationId; }
    public String getSourceType() { return sourceType; }
    public String getTargetType() { return targetType; }
    public String getSourceTopic() { return sourceTopic; }
    public String getTargetTopic() { return targetTopic; }
    public String getPayload() { return payload; }
    public String getHeaders() { return headers; }
    public String getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }
    public int getRetryCount() { return retryCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    // Setters
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public void setBridgeId(String bridgeId) { this.bridgeId = bridgeId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public void setSourceTopic(String sourceTopic) { this.sourceTopic = sourceTopic; }
    public void setTargetTopic(String targetTopic) { this.targetTopic = targetTopic; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setHeaders(String headers) { this.headers = headers; }
    public void setStatus(String status) { this.status = status; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}

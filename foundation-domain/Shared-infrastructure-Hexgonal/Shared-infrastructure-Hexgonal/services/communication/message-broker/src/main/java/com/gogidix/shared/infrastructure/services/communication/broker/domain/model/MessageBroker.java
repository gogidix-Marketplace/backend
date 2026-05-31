package com.gogidix.shared.infrastructure.services.communication.broker.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "broker_messages")
public class MessageBroker {
    
    @Id
    private String id;
    
    @Indexed
    private String tenantId;
    
    @Indexed
    private String topic;
    
    @Indexed
    private String messageType;
    
    private String payload;
    
    private Map<String, Object> headers;
    
    @Indexed
    private String status; // PENDING, PUBLISHED, FAILED
    
    @Indexed
    private LocalDateTime createdAt;
    
    @Indexed
    private LocalDateTime publishedAt;
    
    private String errorMessage;
    
    private Integer retryCount;
    
    @Indexed
    private String correlationId;
    
    private String replyTo;
    
    protected MessageBroker() {
    }
    
    public MessageBroker(String tenantId, String topic, String messageType, String payload) {
        this.tenantId = tenantId;
        this.topic = topic;
        this.messageType = messageType;
        this.payload = payload;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.retryCount = 0;
    }
    
    public void markAsPublished() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
    }
    
    public void markAsFailed(String errorMessage) {
        this.status = "FAILED";
        this.errorMessage = errorMessage;
    }
    
    public void incrementRetry() {
        this.retryCount++;
    }
    
    public boolean canRetry(int maxRetries) {
        return this.retryCount < maxRetries && "FAILED".equals(this.status);
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    
    public Map<String, Object> getHeaders() { return headers; }
    public void setHeaders(Map<String, Object> headers) { this.headers = headers; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    
    public String getReplyTo() { return replyTo; }
    public void setReplyTo(String replyTo) { this.replyTo = replyTo; }
}

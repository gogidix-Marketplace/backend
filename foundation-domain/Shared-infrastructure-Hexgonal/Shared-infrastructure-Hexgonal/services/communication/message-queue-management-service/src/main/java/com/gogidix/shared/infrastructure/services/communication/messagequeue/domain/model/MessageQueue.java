package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
/**
 * Message Queue entity with multi-tenant support.
 */
@Document(collection = "message_queues")
public class MessageQueue {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String name;
    private String description;
    @Indexed
    private String type; // FIFO, STANDARD, DEAD_LETTER
    @Indexed
    private String status; // ACTIVE, INACTIVE, DELETING
    @Indexed
    private String region;
    private Long maxSize; // Maximum size in MB
    private Long messageRetentionPeriod; // in seconds
    private Integer maxReceiveCount;
    private Long visibilityTimeout; // in seconds
    private Integer deliveryDelay; // in seconds
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    protected MessageQueue() {
        // MongoDB
    }
    public MessageQueue(TenantId tenantId, String name, String type) {
        this.tenantId = tenantId;
        this.name = name;
        this.type = type;
        this.status = "ACTIVE";
        this.visibilityTimeout = 30L;
        this.deliveryDelay = 0;
        this.maxReceiveCount = 3;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public Long getMaxSize() {
        return maxSize;
    }
    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }
    public Long getMessageRetentionPeriod() {
        return messageRetentionPeriod;
    }
    public void setMessageRetentionPeriod(Long messageRetentionPeriod) {
        this.messageRetentionPeriod = messageRetentionPeriod;
    }
    public Integer getMaxReceiveCount() {
        return maxReceiveCount;
    }
    public void setMaxReceiveCount(Integer maxReceiveCount) {
        this.maxReceiveCount = maxReceiveCount;
    }
    public Long getVisibilityTimeout() {
        return visibilityTimeout;
    }
    public void setVisibilityTimeout(Long visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
    }
    public Integer getDeliveryDelay() {
        return deliveryDelay;
    }
    public void setDeliveryDelay(Integer deliveryDelay) {
        this.deliveryDelay = deliveryDelay;
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
}

package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;
/**
 * Queue Message entity with multi-tenant support.
 */
@Document(collection = "queue_messages")
public class QueueMessage {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String queueId;
    @Indexed
    private String queueName;
    @Indexed
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private String content;
    @Indexed
    private String messageType;
    private Map<String, String> headers;
    @Indexed
    private Integer receiveCount;
    private String receiptHandle;
    private Long delayUntil; // Epoch milliseconds
    private Long visibilityUntil; // Epoch milliseconds
    @Indexed
    private String priority; // HIGH, MEDIUM, LOW
    private String deduplicationId;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    protected QueueMessage() {
        // MongoDB
    }
    public QueueMessage(TenantId tenantId, String queueId, String queueName, String content) {
        this.tenantId = tenantId;
        this.queueId = queueId;
        this.queueName = queueName;
        this.content = content;
        this.status = "PENDING";
        this.receiveCount = 0;
        this.priority = "MEDIUM";
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
    public String getQueueId() {
        return queueId;
    }
    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }
    public String getQueueName() {
        return queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public Integer getReceiveCount() {
        return receiveCount;
    }
    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }
    public String getReceiptHandle() {
        return receiptHandle;
    }
    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }
    public Long getDelayUntil() {
        return delayUntil;
    }
    public void setDelayUntil(Long delayUntil) {
        this.delayUntil = delayUntil;
    }
    public Long getVisibilityUntil() {
        return visibilityUntil;
    }
    public void setVisibilityUntil(Long visibilityUntil) {
        this.visibilityUntil = visibilityUntil;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getDeduplicationId() {
        return deduplicationId;
    }
    public void setDeduplicationId(String deduplicationId) {
        this.deduplicationId = deduplicationId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    /**
     * Check if message is ready to be processed
     */
    public boolean isReady() {
        long now = System.currentTimeMillis();
        return "PENDING".equals(status) &&
               (delayUntil == null || delayUntil <= now) &&
               (visibilityUntil == null || visibilityUntil <= now);
    }
    /**
     * Check if message is visible
     */
    public boolean isVisible() {
        long now = System.currentTimeMillis();
        return visibilityUntil == null || visibilityUntil <= now;
    }
    /**
     * Increment receive count
     */
    public void incrementReceiveCount() {
        this.receiveCount = (this.receiveCount != null ? this.receiveCount : 0) + 1;
    }
}

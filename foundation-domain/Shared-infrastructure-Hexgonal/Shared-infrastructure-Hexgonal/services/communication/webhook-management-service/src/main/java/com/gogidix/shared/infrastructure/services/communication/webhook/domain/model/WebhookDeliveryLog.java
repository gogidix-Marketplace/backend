package com.gogidix.shared.infrastructure.services.communication.webhook.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Webhook Delivery Log entity with multi-tenant support.
 */
@Document(collection = "webhook_delivery_logs")
public class WebhookDeliveryLog {
    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed
    private String webhookId;

    @Indexed
    private String webhookName;

    @Indexed
    private String eventType;

    private String payload;

    @Indexed
    private String status; // PENDING, SUCCESS, FAILED, RETRYING

    private Integer httpStatusCode;

    private String response;

    private String errorMessage;

    @Indexed
    private String attemptId;

    private Integer attemptNumber;

    private Long duration; // in milliseconds

    private String signature; // HMAC signature for verification

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    protected WebhookDeliveryLog() {
        // MongoDB
    }

    public WebhookDeliveryLog(TenantId tenantId, String webhookId, String webhookName,
                             String eventType, String payload, Integer attemptNumber) {
        this.tenantId = tenantId;
        this.webhookId = webhookId;
        this.webhookName = webhookName;
        this.eventType = eventType;
        this.payload = payload;
        this.attemptNumber = attemptNumber;
        this.status = "PENDING";
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

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public String getWebhookName() {
        return webhookName;
    }

    public void setWebhookName(String webhookName) {
        this.webhookName = webhookName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    /**
     * Check if delivery was successful
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(status);
    }

    /**
     * Check if delivery failed
     */
    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    /**
     * Check if delivery can be retried
     */
    public boolean canRetry() {
        return "FAILED".equals(status) || "PENDING".equals(status);
    }
}

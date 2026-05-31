package com.gogidix.shared.infrastructure.services.communication.sms.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * MongoDB Document: SmsMessage
 * Represents SMS messages with multi-tenant support.
 */
@Document(collection = "sms_messages")
public class SmsMessage {

    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed
    private String phoneNumber;

    private String countryCode;

    @Indexed
    private String status; // PENDING, SENT, DELIVERED, FAILED, RETRYING

    private String message;
    private String templateName;
    private String provider; // TWILIO, AWS_SNS, MESSAGEBIRD, SINCH

    @Indexed
    private String campaignId;

    private String externalMessageId;
    private String errorMessage;

    private int retryCount;
    private int maxRetries = 3;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;

    public SmsMessage() {}

    public SmsMessage(TenantId tenantId, String phoneNumber, String message) {
        this.tenantId = tenantId;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.status = "PENDING";
        this.retryCount = 0;
    }

    // Getters
    public TenantId getTenantId() { return tenantId; }
    public String getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCountryCode() { return countryCode; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getTemplateName() { return templateName; }
    public String getProvider() { return provider; }
    public String getCampaignId() { return campaignId; }
    public String getExternalMessageId() { return externalMessageId; }
    public String getErrorMessage() { return errorMessage; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getSentAt() { return sentAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }
    public void setExternalMessageId(String externalMessageId) { this.externalMessageId = externalMessageId; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
}

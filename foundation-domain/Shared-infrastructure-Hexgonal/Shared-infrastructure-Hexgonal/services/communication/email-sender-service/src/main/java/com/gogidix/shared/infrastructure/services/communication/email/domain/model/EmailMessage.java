package com.gogidix.shared.infrastructure.services.communication.email.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * MongoDB Document: EmailMessage
 * Represents email messages with multi-tenant support.
 */
@Document(collection = "email_messages")
public class EmailMessage {

    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed
    private String to;

    private String cc;
    private String bcc;

    @Indexed
    private String subject;

    private String body;
    private String templateName;

    @Indexed
    private String status; // PENDING, SENT, FAILED, RETRYING

    private int retryCount;
    private int maxRetries = 3;

    private String errorMessage;
    private String provider; // SMTP, SENDGRID, AWS_SES, MAILGUN

    @Indexed
    private String campaignId;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    public EmailMessage() {}

    public EmailMessage(TenantId tenantId, String to, String subject, String body) {
        this.tenantId = tenantId;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.status = "PENDING";
        this.retryCount = 0;
    }

    // Getters
    public TenantId getTenantId() { return tenantId; }
    public String getId() { return id; }
    public String getTo() { return to; }
    public String getCc() { return cc; }
    public String getBcc() { return bcc; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public String getTemplateName() { return templateName; }
    public String getStatus() { return status; }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public String getErrorMessage() { return errorMessage; }
    public String getProvider() { return provider; }
    public String getCampaignId() { return campaignId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getSentAt() { return sentAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public void setTo(String to) { this.to = to; }
    public void setCc(String cc) { this.cc = cc; }
    public void setBcc(String bcc) { this.bcc = bcc; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setBody(String body) { this.body = body; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }
    public void setStatus(String status) { this.status = status; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

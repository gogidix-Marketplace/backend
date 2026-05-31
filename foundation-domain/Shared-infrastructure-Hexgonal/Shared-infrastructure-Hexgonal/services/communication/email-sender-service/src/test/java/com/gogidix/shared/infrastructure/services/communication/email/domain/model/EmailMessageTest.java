package com.gogidix.shared.infrastructure.services.communication.email.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EmailMessage domain model.
 */
@DisplayName("Email Message Domain Model Tests")
class EmailMessageTest {

    @Test
    @DisplayName("Should create email message with constructor")
    void shouldCreateEmailMessageWithConstructor() {
        TenantId tenantId = new TenantId("tenant123");
        EmailMessage message = new EmailMessage(tenantId, "test@example.com", "Test Subject", "Test Body");

        assertEquals(tenantId, message.getTenantId());
        assertEquals("test@example.com", message.getTo());
        assertEquals("Test Subject", message.getSubject());
        assertEquals("Test Body", message.getBody());
        assertEquals("PENDING", message.getStatus());
        assertEquals(0, message.getRetryCount());
    }

    @Test
    @DisplayName("Should create email message with no-args constructor")
    void shouldCreateEmailMessageWithNoArgsConstructor() {
        EmailMessage message = new EmailMessage();

        assertNotNull(message);
        assertNull(message.getTenantId());
        assertNull(message.getTo());
        assertNull(message.getSubject());
        assertNull(message.getBody());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        EmailMessage message = new EmailMessage();

        message.setTenantId(tenantId);
        message.setTo("recipient@example.com");
        message.setCc("cc@example.com");
        message.setBcc("bcc@example.com");
        message.setSubject("Test Subject");
        message.setBody("Test Body");
        message.setTemplateName("welcome-template");
        message.setStatus("SENT");
        message.setRetryCount(1);
        message.setMaxRetries(5);
        message.setErrorMessage("Error occurred");
        message.setProvider("SMTP");
        message.setCampaignId("campaign123");
        message.setSentAt(LocalDateTime.now());

        assertEquals(tenantId, message.getTenantId());
        assertEquals("recipient@example.com", message.getTo());
        assertEquals("cc@example.com", message.getCc());
        assertEquals("bcc@example.com", message.getBcc());
        assertEquals("Test Subject", message.getSubject());
        assertEquals("Test Body", message.getBody());
        assertEquals("welcome-template", message.getTemplateName());
        assertEquals("SENT", message.getStatus());
        assertEquals(1, message.getRetryCount());
        assertEquals(5, message.getMaxRetries());
        assertEquals("Error occurred", message.getErrorMessage());
        assertEquals("SMTP", message.getProvider());
        assertEquals("campaign123", message.getCampaignId());
        assertNotNull(message.getSentAt());
    }

    @Test
    @DisplayName("Should handle null tenant ID")
    void shouldHandleNullTenantId() {
        EmailMessage message = new EmailMessage(null, "test@example.com", "Subject", "Body");

        assertNull(message.getTenantId());
        assertEquals("test@example.com", message.getTo());
    }

    @Test
    @DisplayName("Should update status correctly")
    void shouldUpdateStatusCorrectly() {
        EmailMessage message = new EmailMessage();
        message.setStatus("PENDING");

        assertEquals("PENDING", message.getStatus());

        message.setStatus("SENT");
        assertEquals("SENT", message.getStatus());

        message.setStatus("FAILED");
        assertEquals("FAILED", message.getStatus());

        message.setStatus("RETRYING");
        assertEquals("RETRYING", message.getStatus());
    }

    @Test
    @DisplayName("Should handle retry count increment")
    void shouldHandleRetryCountIncrement() {
        EmailMessage message = new EmailMessage();
        message.setRetryCount(0);

        assertEquals(0, message.getRetryCount());

        message.setRetryCount(message.getRetryCount() + 1);
        assertEquals(1, message.getRetryCount());

        message.setRetryCount(message.getRetryCount() + 1);
        assertEquals(2, message.getRetryCount());
    }

    @Test
    @DisplayName("Should handle custom max retries")
    void shouldHandleCustomMaxRetries() {
        EmailMessage message = new EmailMessage();
        message.setMaxRetries(10);

        assertEquals(10, message.getMaxRetries());
    }

    @Test
    @DisplayName("Should verify different providers")
    void shouldVerifyDifferentProviders() {
        EmailMessage message = new EmailMessage();

        message.setProvider("SMTP");
        assertEquals("SMTP", message.getProvider());

        message.setProvider("SENDGRID");
        assertEquals("SENDGRID", message.getProvider());

        message.setProvider("AWS_SES");
        assertEquals("AWS_SES", message.getProvider());

        message.setProvider("MAILGUN");
        assertEquals("MAILGUN", message.getProvider());
    }

    @Test
    @DisplayName("Should handle campaign ID")
    void shouldHandleCampaignId() {
        EmailMessage message = new EmailMessage();
        message.setCampaignId("campaign-abc-123");

        assertEquals("campaign-abc-123", message.getCampaignId());
    }

    @Test
    @DisplayName("Should handle error messages")
    void shouldHandleErrorMessages() {
        EmailMessage message = new EmailMessage();
        message.setErrorMessage("Connection timeout");

        assertEquals("Connection timeout", message.getErrorMessage());

        message.setErrorMessage(null);
        assertNull(message.getErrorMessage());
    }

    @Test
    @DisplayName("Should handle CC and BCC fields")
    void shouldHandleCcAndBccFields() {
        EmailMessage message = new EmailMessage();
        message.setCc("cc1@example.com,cc2@example.com");
        message.setBcc("bcc@example.com");

        assertEquals("cc1@example.com,cc2@example.com", message.getCc());
        assertEquals("bcc@example.com", message.getBcc());
    }

    @Test
    @DisplayName("Should handle sent at timestamp")
    void shouldHandleSentAtTimestamp() {
        LocalDateTime sentTime = LocalDateTime.now();
        EmailMessage message = new EmailMessage();
        message.setSentAt(sentTime);

        assertEquals(sentTime, message.getSentAt());
    }
}

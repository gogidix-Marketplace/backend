package com.gogidix.shared.infrastructure.services.communication.sms.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SmsMessage domain model.
 */
@DisplayName("SMS Message Domain Model Tests")
class SmsMessageTest {

    @Test
    @DisplayName("Should create SMS message with constructor")
    void shouldCreateSmsMessageWithConstructor() {
        TenantId tenantId = new TenantId("tenant123");
        SmsMessage message = new SmsMessage(tenantId, "+1234567890", "Test message");

        assertEquals(tenantId, message.getTenantId());
        assertEquals("+1234567890", message.getPhoneNumber());
        assertEquals("Test message", message.getMessage());
        assertEquals("PENDING", message.getStatus());
        assertEquals(0, message.getRetryCount());
    }

    @Test
    @DisplayName("Should create SMS message with no-args constructor")
    void shouldCreateSmsMessageWithNoArgsConstructor() {
        SmsMessage message = new SmsMessage();

        assertNotNull(message);
        assertNull(message.getTenantId());
        assertNull(message.getPhoneNumber());
        assertNull(message.getMessage());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        TenantId tenantId = new TenantId("tenant123");
        LocalDateTime now = LocalDateTime.now();

        SmsMessage message = new SmsMessage();
        message.setTenantId(tenantId);
        message.setPhoneNumber("+1234567890");
        message.setCountryCode("+1");
        message.setStatus("SENT");
        message.setMessage("Test message");
        message.setTemplateName("verify-template");
        message.setProvider("TWILIO");
        message.setCampaignId("campaign123");
        message.setExternalMessageId("ext123");
        message.setErrorMessage("Error occurred");
        message.setRetryCount(2);
        message.setMaxRetries(5);
        message.setSentAt(now);
        message.setDeliveredAt(now);

        assertEquals(tenantId, message.getTenantId());
        assertEquals("+1234567890", message.getPhoneNumber());
        assertEquals("+1", message.getCountryCode());
        assertEquals("SENT", message.getStatus());
        assertEquals("Test message", message.getMessage());
        assertEquals("verify-template", message.getTemplateName());
        assertEquals("TWILIO", message.getProvider());
        assertEquals("campaign123", message.getCampaignId());
        assertEquals("ext123", message.getExternalMessageId());
        assertEquals("Error occurred", message.getErrorMessage());
        assertEquals(2, message.getRetryCount());
        assertEquals(5, message.getMaxRetries());
        assertEquals(now, message.getSentAt());
        assertEquals(now, message.getDeliveredAt());
    }

    @Test
    @DisplayName("Should handle null tenant ID")
    void shouldHandleNullTenantId() {
        SmsMessage message = new SmsMessage(null, "+1234567890", "Message");

        assertNull(message.getTenantId());
        assertEquals("+1234567890", message.getPhoneNumber());
    }

    @Test
    @DisplayName("Should update status correctly")
    void shouldUpdateStatusCorrectly() {
        SmsMessage message = new SmsMessage();
        message.setStatus("PENDING");

        assertEquals("PENDING", message.getStatus());

        message.setStatus("SENT");
        assertEquals("SENT", message.getStatus());

        message.setStatus("DELIVERED");
        assertEquals("DELIVERED", message.getStatus());

        message.setStatus("FAILED");
        assertEquals("FAILED", message.getStatus());

        message.setStatus("RETRYING");
        assertEquals("RETRYING", message.getStatus());
    }

    @Test
    @DisplayName("Should handle different providers")
    void shouldHandleDifferentProviders() {
        String[] providers = {"TWILIO", "AWS_SNS", "MESSAGEBIRD", "SINCH"};

        for (String provider : providers) {
            SmsMessage message = new SmsMessage();
            message.setProvider(provider);

            assertEquals(provider, message.getProvider());
        }
    }

    @Test
    @DisplayName("Should handle retry count increment")
    void shouldHandleRetryCountIncrement() {
        SmsMessage message = new SmsMessage();
        message.setRetryCount(0);

        assertEquals(0, message.getRetryCount());

        message.setRetryCount(message.getRetryCount() + 1);
        assertEquals(1, message.getRetryCount());
    }

    @Test
    @DisplayName("Should handle custom max retries")
    void shouldHandleCustomMaxRetries() {
        SmsMessage message = new SmsMessage();
        message.setMaxRetries(10);

        assertEquals(10, message.getMaxRetries());
    }

    @Test
    @DisplayName("Should handle country code")
    void shouldHandleCountryCode() {
        SmsMessage message = new SmsMessage();
        message.setCountryCode("+1");

        assertEquals("+1", message.getCountryCode());

        message.setCountryCode("+44");
        assertEquals("+44", message.getCountryCode());
    }

    @Test
    @DisplayName("Should handle external message ID")
    void shouldHandleExternalMessageId() {
        SmsMessage message = new SmsMessage();
        message.setExternalMessageId("twilio-msg-123");

        assertEquals("twilio-msg-123", message.getExternalMessageId());
    }

    @Test
    @DisplayName("Should handle campaign ID")
    void shouldHandleCampaignId() {
        SmsMessage message = new SmsMessage();
        message.setCampaignId("sms-campaign-456");

        assertEquals("sms-campaign-456", message.getCampaignId());
    }

    @Test
    @DisplayName("Should handle timestamps")
    void shouldHandleTimestamps() {
        LocalDateTime sentTime = LocalDateTime.now();
        LocalDateTime deliveredTime = sentTime.plusMinutes(1);

        SmsMessage message = new SmsMessage();
        message.setSentAt(sentTime);
        message.setDeliveredAt(deliveredTime);

        assertEquals(sentTime, message.getSentAt());
        assertEquals(deliveredTime, message.getDeliveredAt());
    }
}

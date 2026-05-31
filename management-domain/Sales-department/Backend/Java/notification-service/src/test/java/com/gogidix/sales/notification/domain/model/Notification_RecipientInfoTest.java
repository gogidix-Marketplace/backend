package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.model.Notification;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class Notification_RecipientInfoTest {

        @Test
    void testBuilder() {
        Notification.RecipientInfo dto = Notification.RecipientInfo.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .deviceToken("test-deviceToken")
            .webhookUrl("test-webhookUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-recipientName", dto.getRecipientName());
        assertEquals("test-recipientType", dto.getRecipientType());
        assertEquals("test-emailAddress", dto.getEmailAddress());
        assertEquals("test-phoneNumber", dto.getPhoneNumber());
        assertEquals("test-deviceToken", dto.getDeviceToken());
        assertEquals("test-webhookUrl", dto.getWebhookUrl());
    }

    @Test
    void testSettersAndGetters() {
        Notification.RecipientInfo dto = new Notification.RecipientInfo();
        dto.setRecipientId("val-recipientId");
        dto.setRecipientName("val-recipientName");
        dto.setRecipientType("val-recipientType");
        dto.setEmailAddress("val-emailAddress");
        dto.setPhoneNumber("val-phoneNumber");
        dto.setDeviceToken("val-deviceToken");
        dto.setWebhookUrl("val-webhookUrl");
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientName", dto.getRecipientName());
        assertEquals("val-recipientType", dto.getRecipientType());
        assertEquals("val-emailAddress", dto.getEmailAddress());
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertEquals("val-deviceToken", dto.getDeviceToken());
        assertEquals("val-webhookUrl", dto.getWebhookUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        Notification.RecipientInfo dto1 = Notification.RecipientInfo.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .deviceToken("test-deviceToken")
            .webhookUrl("test-webhookUrl")
            .build();
        Notification.RecipientInfo dto2 = Notification.RecipientInfo.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .deviceToken("test-deviceToken")
            .webhookUrl("test-webhookUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Notification.RecipientInfo dto = Notification.RecipientInfo.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .deviceToken("test-deviceToken")
            .webhookUrl("test-webhookUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
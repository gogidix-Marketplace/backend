package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationResponseDto;
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
class NotificationResponseDto_RecipientInfoDtoTest {

        @Test
    void testBuilder() {
        NotificationResponseDto.RecipientInfoDto dto = NotificationResponseDto.RecipientInfoDto.builder()
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
        NotificationResponseDto.RecipientInfoDto dto = new NotificationResponseDto.RecipientInfoDto();
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
        NotificationResponseDto.RecipientInfoDto dto1 = NotificationResponseDto.RecipientInfoDto.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .deviceToken("test-deviceToken")
            .webhookUrl("test-webhookUrl")
            .build();
        NotificationResponseDto.RecipientInfoDto dto2 = NotificationResponseDto.RecipientInfoDto.builder()
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
        NotificationResponseDto.RecipientInfoDto dto = NotificationResponseDto.RecipientInfoDto.builder()
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
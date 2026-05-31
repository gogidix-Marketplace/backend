package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.MessageResponseDto;
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
class MessageResponseDto_RecipientInfoDtoTest {

        @Test
    void testBuilder() {
        MessageResponseDto.RecipientInfoDto dto = MessageResponseDto.RecipientInfoDto.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveryStatus("test-deliveryStatus")
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-recipientId", dto.getRecipientId());
        assertEquals("test-recipientName", dto.getRecipientName());
        assertEquals("test-recipientType", dto.getRecipientType());
        assertEquals("test-emailAddress", dto.getEmailAddress());
        assertEquals("test-phoneNumber", dto.getPhoneNumber());
        assertTrue(dto.getIsRead());
        assertEquals("test-deliveryStatus", dto.getDeliveryStatus());
    }

    @Test
    void testSettersAndGetters() {
        MessageResponseDto.RecipientInfoDto dto = new MessageResponseDto.RecipientInfoDto();
        dto.setRecipientId("val-recipientId");
        dto.setRecipientName("val-recipientName");
        dto.setRecipientType("val-recipientType");
        dto.setEmailAddress("val-emailAddress");
        dto.setPhoneNumber("val-phoneNumber");
        dto.setIsRead(true);
        dto.setDeliveryStatus("val-deliveryStatus");
        assertEquals("val-recipientId", dto.getRecipientId());
        assertEquals("val-recipientName", dto.getRecipientName());
        assertEquals("val-recipientType", dto.getRecipientType());
        assertEquals("val-emailAddress", dto.getEmailAddress());
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertTrue(dto.getIsRead());
        assertEquals("val-deliveryStatus", dto.getDeliveryStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageResponseDto.RecipientInfoDto dto1 = MessageResponseDto.RecipientInfoDto.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveryStatus("test-deliveryStatus")
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        MessageResponseDto.RecipientInfoDto dto2 = MessageResponseDto.RecipientInfoDto.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveryStatus("test-deliveryStatus")
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageResponseDto.RecipientInfoDto dto = MessageResponseDto.RecipientInfoDto.builder()
                        .recipientId("test-recipientId")
            .recipientName("test-recipientName")
            .recipientType("test-recipientType")
            .emailAddress("test-emailAddress")
            .phoneNumber("test-phoneNumber")
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveryStatus("test-deliveryStatus")
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
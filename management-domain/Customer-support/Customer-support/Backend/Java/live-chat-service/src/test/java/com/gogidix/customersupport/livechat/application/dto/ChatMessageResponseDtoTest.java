package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageResponseDto;
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
class ChatMessageResponseDtoTest {

        @Test
    void testBuilder() {
        ChatMessageResponseDto dto = ChatMessageResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .messageId("test-messageId")
            .senderType(ChatMessageResponseDto.SenderTypeDto.CUSTOMER)
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .messageType(ChatMessageResponseDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isDeleted(true)
            .editedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .replyToMessageId("test-replyToMessageId")
            .metadata(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-sessionId", dto.getSessionId());
        assertEquals("test-messageId", dto.getMessageId());
        assertEquals(ChatMessageResponseDto.SenderTypeDto.CUSTOMER, dto.getSenderType());
        assertEquals("test-senderId", dto.getSenderId());
        assertEquals("test-senderName", dto.getSenderName());
        assertEquals("test-messageContent", dto.getMessageContent());
        assertEquals(ChatMessageResponseDto.MessageTypeDto.TEXT, dto.getMessageType());
        assertTrue(dto.getIsDeleted());
        assertEquals("test-replyToMessageId", dto.getReplyToMessageId());
    }

    @Test
    void testSettersAndGetters() {
        ChatMessageResponseDto dto = new ChatMessageResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setSessionId("val-sessionId");
        dto.setMessageId("val-messageId");
        dto.setSenderType(ChatMessageResponseDto.SenderTypeDto.CUSTOMER);
        dto.setSenderId("val-senderId");
        dto.setSenderName("val-senderName");
        dto.setMessageContent("val-messageContent");
        dto.setMessageType(ChatMessageResponseDto.MessageTypeDto.TEXT);
        dto.setIsDeleted(true);
        dto.setReplyToMessageId("val-replyToMessageId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-sessionId", dto.getSessionId());
        assertEquals("val-messageId", dto.getMessageId());
        assertEquals(ChatMessageResponseDto.SenderTypeDto.CUSTOMER, dto.getSenderType());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-senderName", dto.getSenderName());
        assertEquals("val-messageContent", dto.getMessageContent());
        assertEquals(ChatMessageResponseDto.MessageTypeDto.TEXT, dto.getMessageType());
        assertTrue(dto.getIsDeleted());
        assertEquals("val-replyToMessageId", dto.getReplyToMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessageResponseDto dto1 = ChatMessageResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .messageId("test-messageId")
            .senderType(ChatMessageResponseDto.SenderTypeDto.CUSTOMER)
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .messageType(ChatMessageResponseDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isDeleted(true)
            .editedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .replyToMessageId("test-replyToMessageId")
            .metadata(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ChatMessageResponseDto dto2 = ChatMessageResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .messageId("test-messageId")
            .senderType(ChatMessageResponseDto.SenderTypeDto.CUSTOMER)
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .messageType(ChatMessageResponseDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isDeleted(true)
            .editedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .replyToMessageId("test-replyToMessageId")
            .metadata(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatMessageResponseDto dto = ChatMessageResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .messageId("test-messageId")
            .senderType(ChatMessageResponseDto.SenderTypeDto.CUSTOMER)
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .messageType(ChatMessageResponseDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isDeleted(true)
            .editedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .replyToMessageId("test-replyToMessageId")
            .metadata(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
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
class MessageResponseDtoTest {

        @Test
    void testBuilder() {
        MessageResponseDto dto = MessageResponseDto.builder()
                        .id("test-id")
            .messageId("test-messageId")
            .tenantId("test-tenantId")
            .conversationId("test-conversationId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .senderType("test-senderType")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(MessageResponseDto.ChannelTypeDto.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .status(MessageResponseDto.MessageStatusDto.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .attachments(Collections.emptyList())
            .parentMessageId("test-parentMessageId")
            .isSystemMessage(true)
            .priority(42)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-messageId", dto.getMessageId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-conversationId", dto.getConversationId());
        assertEquals("test-senderId", dto.getSenderId());
        assertEquals("test-senderName", dto.getSenderName());
        assertEquals("test-senderType", dto.getSenderType());
        assertEquals(MessageResponseDto.ChannelTypeDto.EMAIL, dto.getChannel());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals(MessageResponseDto.MessageStatusDto.DRAFT, dto.getStatus());
        assertTrue(dto.getIsRead());
        assertEquals("test-readBy", dto.getReadBy());
        assertEquals("test-parentMessageId", dto.getParentMessageId());
        assertTrue(dto.getIsSystemMessage());
        assertEquals(42, dto.getPriority());
        assertEquals("test-externalMessageId", dto.getExternalMessageId());
    }

    @Test
    void testSettersAndGetters() {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId("val-id");
        dto.setMessageId("val-messageId");
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setSenderId("val-senderId");
        dto.setSenderName("val-senderName");
        dto.setSenderType("val-senderType");
        dto.setChannel(MessageResponseDto.ChannelTypeDto.EMAIL);
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setTemplateId("val-templateId");
        dto.setStatus(MessageResponseDto.MessageStatusDto.DRAFT);
        dto.setIsRead(true);
        dto.setReadBy("val-readBy");
        dto.setParentMessageId("val-parentMessageId");
        dto.setIsSystemMessage(true);
        dto.setPriority(99);
        dto.setExternalMessageId("val-externalMessageId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-messageId", dto.getMessageId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-senderName", dto.getSenderName());
        assertEquals("val-senderType", dto.getSenderType());
        assertEquals(MessageResponseDto.ChannelTypeDto.EMAIL, dto.getChannel());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals(MessageResponseDto.MessageStatusDto.DRAFT, dto.getStatus());
        assertTrue(dto.getIsRead());
        assertEquals("val-readBy", dto.getReadBy());
        assertEquals("val-parentMessageId", dto.getParentMessageId());
        assertTrue(dto.getIsSystemMessage());
        assertEquals(99, dto.getPriority());
        assertEquals("val-externalMessageId", dto.getExternalMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageResponseDto dto1 = MessageResponseDto.builder()
                        .id("test-id")
            .messageId("test-messageId")
            .tenantId("test-tenantId")
            .conversationId("test-conversationId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .senderType("test-senderType")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(MessageResponseDto.ChannelTypeDto.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .status(MessageResponseDto.MessageStatusDto.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .attachments(Collections.emptyList())
            .parentMessageId("test-parentMessageId")
            .isSystemMessage(true)
            .priority(42)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        MessageResponseDto dto2 = MessageResponseDto.builder()
                        .id("test-id")
            .messageId("test-messageId")
            .tenantId("test-tenantId")
            .conversationId("test-conversationId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .senderType("test-senderType")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(MessageResponseDto.ChannelTypeDto.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .status(MessageResponseDto.MessageStatusDto.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .attachments(Collections.emptyList())
            .parentMessageId("test-parentMessageId")
            .isSystemMessage(true)
            .priority(42)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageResponseDto dto = MessageResponseDto.builder()
                        .id("test-id")
            .messageId("test-messageId")
            .tenantId("test-tenantId")
            .conversationId("test-conversationId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .senderType("test-senderType")
            .recipientIds(Collections.emptyList())
            .recipients(Collections.emptyList())
            .channel(MessageResponseDto.ChannelTypeDto.EMAIL)
            .subject("test-subject")
            .content("test-content")
            .templateId("test-templateId")
            .status(MessageResponseDto.MessageStatusDto.DRAFT)
            .isRead(true)
            .readAt(Instant.parse("2025-01-15T10:00:00Z"))
            .readBy("test-readBy")
            .attachments(Collections.emptyList())
            .parentMessageId("test-parentMessageId")
            .isSystemMessage(true)
            .priority(42)
            .scheduledAt(Instant.parse("2025-01-15T10:00:00Z"))
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deliveredAt(Instant.parse("2025-01-15T10:00:00Z"))
            .externalMessageId("test-externalMessageId")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
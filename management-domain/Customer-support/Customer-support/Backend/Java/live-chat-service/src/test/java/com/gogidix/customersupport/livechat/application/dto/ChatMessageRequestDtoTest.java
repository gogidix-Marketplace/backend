package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageRequestDto;
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
class ChatMessageRequestDtoTest {

        @Test
    void testBuilder() {
        ChatMessageRequestDto dto = ChatMessageRequestDto.builder()
                        .sessionId("test-sessionId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .senderType(ChatMessageRequestDto.SenderTypeDto.CUSTOMER)
            .messageType(ChatMessageRequestDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .replyToMessageId("test-replyToMessageId")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .build();
        assertNotNull(dto);
        assertEquals("test-sessionId", dto.getSessionId());
        assertEquals("test-senderId", dto.getSenderId());
        assertEquals("test-senderName", dto.getSenderName());
        assertEquals("test-messageContent", dto.getMessageContent());
        assertEquals(ChatMessageRequestDto.SenderTypeDto.CUSTOMER, dto.getSenderType());
        assertEquals(ChatMessageRequestDto.MessageTypeDto.TEXT, dto.getMessageType());
        assertEquals("test-replyToMessageId", dto.getReplyToMessageId());
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
    }

    @Test
    void testSettersAndGetters() {
        ChatMessageRequestDto dto = new ChatMessageRequestDto();
        dto.setSessionId("val-sessionId");
        dto.setSenderId("val-senderId");
        dto.setSenderName("val-senderName");
        dto.setMessageContent("val-messageContent");
        dto.setSenderType(ChatMessageRequestDto.SenderTypeDto.CUSTOMER);
        dto.setMessageType(ChatMessageRequestDto.MessageTypeDto.TEXT);
        dto.setReplyToMessageId("val-replyToMessageId");
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        assertEquals("val-sessionId", dto.getSessionId());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-senderName", dto.getSenderName());
        assertEquals("val-messageContent", dto.getMessageContent());
        assertEquals(ChatMessageRequestDto.SenderTypeDto.CUSTOMER, dto.getSenderType());
        assertEquals(ChatMessageRequestDto.MessageTypeDto.TEXT, dto.getMessageType());
        assertEquals("val-replyToMessageId", dto.getReplyToMessageId());
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessageRequestDto dto1 = ChatMessageRequestDto.builder()
                        .sessionId("test-sessionId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .senderType(ChatMessageRequestDto.SenderTypeDto.CUSTOMER)
            .messageType(ChatMessageRequestDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .replyToMessageId("test-replyToMessageId")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .build();
        ChatMessageRequestDto dto2 = ChatMessageRequestDto.builder()
                        .sessionId("test-sessionId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .senderType(ChatMessageRequestDto.SenderTypeDto.CUSTOMER)
            .messageType(ChatMessageRequestDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .replyToMessageId("test-replyToMessageId")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatMessageRequestDto dto = ChatMessageRequestDto.builder()
                        .sessionId("test-sessionId")
            .senderId("test-senderId")
            .senderName("test-senderName")
            .messageContent("test-messageContent")
            .senderType(ChatMessageRequestDto.SenderTypeDto.CUSTOMER)
            .messageType(ChatMessageRequestDto.MessageTypeDto.TEXT)
            .attachments(Collections.emptyList())
            .replyToMessageId("test-replyToMessageId")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
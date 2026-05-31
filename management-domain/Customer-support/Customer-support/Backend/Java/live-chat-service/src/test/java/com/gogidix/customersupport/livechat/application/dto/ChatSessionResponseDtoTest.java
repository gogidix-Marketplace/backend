package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatSessionResponseDto;
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
class ChatSessionResponseDtoTest {

        @Test
    void testBuilder() {
        ChatSessionResponseDto dto = ChatSessionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .status(ChatSessionResponseDto.ChatStatusDto.WAITING)
            .channel("test-channel")
            .queuePosition(42)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .endedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationSeconds(42L)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waitingTimeSeconds(42L)
            .rating(42)
            .feedback("test-feedback")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .messageCount(42)
            .customerMessageCount(42)
            .agentMessageCount(42)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .preChatSurveyData(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-sessionId", dto.getSessionId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals(ChatSessionResponseDto.ChatStatusDto.WAITING, dto.getStatus());
        assertEquals("test-channel", dto.getChannel());
        assertEquals(42, dto.getQueuePosition());
        assertEquals(42L, dto.getDurationSeconds());
        assertEquals(42L, dto.getWaitingTimeSeconds());
        assertEquals(42, dto.getRating());
        assertEquals("test-feedback", dto.getFeedback());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-relatedTicketId", dto.getRelatedTicketId());
        assertEquals(42, dto.getMessageCount());
        assertEquals(42, dto.getCustomerMessageCount());
        assertEquals(42, dto.getAgentMessageCount());
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
        assertEquals("test-referrer", dto.getReferrer());
        assertEquals("test-location", dto.getLocation());
    }

    @Test
    void testSettersAndGetters() {
        ChatSessionResponseDto dto = new ChatSessionResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setSessionId("val-sessionId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setStatus(ChatSessionResponseDto.ChatStatusDto.WAITING);
        dto.setChannel("val-channel");
        dto.setQueuePosition(99);
        dto.setRating(99);
        dto.setFeedback("val-feedback");
        dto.setCategory("val-category");
        dto.setRelatedTicketId("val-relatedTicketId");
        dto.setMessageCount(99);
        dto.setCustomerMessageCount(99);
        dto.setAgentMessageCount(99);
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        dto.setReferrer("val-referrer");
        dto.setLocation("val-location");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-sessionId", dto.getSessionId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals(ChatSessionResponseDto.ChatStatusDto.WAITING, dto.getStatus());
        assertEquals("val-channel", dto.getChannel());
        assertEquals(99, dto.getQueuePosition());
        assertEquals(99, dto.getRating());
        assertEquals("val-feedback", dto.getFeedback());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-relatedTicketId", dto.getRelatedTicketId());
        assertEquals(99, dto.getMessageCount());
        assertEquals(99, dto.getCustomerMessageCount());
        assertEquals(99, dto.getAgentMessageCount());
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
        assertEquals("val-referrer", dto.getReferrer());
        assertEquals("val-location", dto.getLocation());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatSessionResponseDto dto1 = ChatSessionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .status(ChatSessionResponseDto.ChatStatusDto.WAITING)
            .channel("test-channel")
            .queuePosition(42)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .endedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationSeconds(42L)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waitingTimeSeconds(42L)
            .rating(42)
            .feedback("test-feedback")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .messageCount(42)
            .customerMessageCount(42)
            .agentMessageCount(42)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .preChatSurveyData(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ChatSessionResponseDto dto2 = ChatSessionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .status(ChatSessionResponseDto.ChatStatusDto.WAITING)
            .channel("test-channel")
            .queuePosition(42)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .endedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationSeconds(42L)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waitingTimeSeconds(42L)
            .rating(42)
            .feedback("test-feedback")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .messageCount(42)
            .customerMessageCount(42)
            .agentMessageCount(42)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .preChatSurveyData(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatSessionResponseDto dto = ChatSessionResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .status(ChatSessionResponseDto.ChatStatusDto.WAITING)
            .channel("test-channel")
            .queuePosition(42)
            .startedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .endedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .durationSeconds(42L)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .waitingTimeSeconds(42L)
            .rating(42)
            .feedback("test-feedback")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .messageCount(42)
            .customerMessageCount(42)
            .agentMessageCount(42)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .preChatSurveyData(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
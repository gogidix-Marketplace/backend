package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.application.dto.response.FeedbackResponseDto;
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
class FeedbackResponseDtoTest {

        @Test
    void testBuilder() {
        FeedbackResponseDto dto = FeedbackResponseDto.builder()
                        .id("test-id")
            .feedbackId("test-feedbackId")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .chatSessionId("test-chatSessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .rating(42)
            .npsScore(42)
            .csatScore(42)
            .categories(Collections.emptyMap())
            .comment("test-comment")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .sourceChannel("test-sourceChannel")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewed(true)
            .reviewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewedBy("test-reviewedBy")
            .tags(Collections.emptyList())
            .followUpRequired(true)
            .responseSent(true)
            .responseSentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-feedbackId", dto.getFeedbackId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-chatSessionId", dto.getChatSessionId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals(42, dto.getRating());
        assertEquals(42, dto.getNpsScore());
        assertEquals(42, dto.getCsatScore());
        assertEquals("test-comment", dto.getComment());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-sourceChannel", dto.getSourceChannel());
        assertTrue(dto.getReviewed());
        assertEquals("test-reviewedBy", dto.getReviewedBy());
        assertTrue(dto.getFollowUpRequired());
        assertTrue(dto.getResponseSent());
    }

    @Test
    void testSettersAndGetters() {
        FeedbackResponseDto dto = new FeedbackResponseDto();
        dto.setId("val-id");
        dto.setFeedbackId("val-feedbackId");
        dto.setTenantId("val-tenantId");
        dto.setTicketId("val-ticketId");
        dto.setChatSessionId("val-chatSessionId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setRating(99);
        dto.setNpsScore(99);
        dto.setCsatScore(99);
        dto.setComment("val-comment");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setSourceChannel("val-sourceChannel");
        dto.setReviewed(true);
        dto.setReviewedBy("val-reviewedBy");
        dto.setFollowUpRequired(true);
        dto.setResponseSent(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-feedbackId", dto.getFeedbackId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-chatSessionId", dto.getChatSessionId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals(99, dto.getRating());
        assertEquals(99, dto.getNpsScore());
        assertEquals(99, dto.getCsatScore());
        assertEquals("val-comment", dto.getComment());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-sourceChannel", dto.getSourceChannel());
        assertTrue(dto.getReviewed());
        assertEquals("val-reviewedBy", dto.getReviewedBy());
        assertTrue(dto.getFollowUpRequired());
        assertTrue(dto.getResponseSent());
    }

    @Test
    void testEqualsAndHashCode() {
        FeedbackResponseDto dto1 = FeedbackResponseDto.builder()
                        .id("test-id")
            .feedbackId("test-feedbackId")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .chatSessionId("test-chatSessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .rating(42)
            .npsScore(42)
            .csatScore(42)
            .categories(Collections.emptyMap())
            .comment("test-comment")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .sourceChannel("test-sourceChannel")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewed(true)
            .reviewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewedBy("test-reviewedBy")
            .tags(Collections.emptyList())
            .followUpRequired(true)
            .responseSent(true)
            .responseSentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        FeedbackResponseDto dto2 = FeedbackResponseDto.builder()
                        .id("test-id")
            .feedbackId("test-feedbackId")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .chatSessionId("test-chatSessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .rating(42)
            .npsScore(42)
            .csatScore(42)
            .categories(Collections.emptyMap())
            .comment("test-comment")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .sourceChannel("test-sourceChannel")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewed(true)
            .reviewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewedBy("test-reviewedBy")
            .tags(Collections.emptyList())
            .followUpRequired(true)
            .responseSent(true)
            .responseSentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        FeedbackResponseDto dto = FeedbackResponseDto.builder()
                        .id("test-id")
            .feedbackId("test-feedbackId")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .chatSessionId("test-chatSessionId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .rating(42)
            .npsScore(42)
            .csatScore(42)
            .categories(Collections.emptyMap())
            .comment("test-comment")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .sourceChannel("test-sourceChannel")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewed(true)
            .reviewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewedBy("test-reviewedBy")
            .tags(Collections.emptyList())
            .followUpRequired(true)
            .responseSent(true)
            .responseSentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
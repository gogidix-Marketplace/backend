package com.gogidix.customersupport.feedback.application.dto.request;

import com.gogidix.customersupport.feedback.application.dto.request.FeedbackRequestDto;
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
class FeedbackRequestDtoTest {

        @Test
    void testBuilder() {
        FeedbackRequestDto dto = FeedbackRequestDto.builder()
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
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
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
    }

    @Test
    void testSettersAndGetters() {
        FeedbackRequestDto dto = new FeedbackRequestDto();
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
    }

    @Test
    void testEqualsAndHashCode() {
        FeedbackRequestDto dto1 = FeedbackRequestDto.builder()
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
            .tags(Collections.emptyList())
            .build();
        FeedbackRequestDto dto2 = FeedbackRequestDto.builder()
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
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        FeedbackRequestDto dto = FeedbackRequestDto.builder()
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
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
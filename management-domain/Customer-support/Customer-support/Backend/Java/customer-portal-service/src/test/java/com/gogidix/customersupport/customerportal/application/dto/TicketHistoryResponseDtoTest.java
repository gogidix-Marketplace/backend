package com.gogidix.customersupport.customerportal.application.dto;

import com.gogidix.customersupport.customerportal.application.dto.TicketHistoryResponseDto;
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
class TicketHistoryResponseDtoTest {

        @Test
    void testBuilder() {
        TicketHistoryResponseDto dto = TicketHistoryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status("test-status")
            .priority("test-priority")
            .category("test-category")
            .channel("test-channel")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .satisfactionRating(42)
            .feedback("test-feedback")
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-ticketNumber", dto.getTicketNumber());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals(42, dto.getSatisfactionRating());
        assertEquals("test-feedback", dto.getFeedback());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
    }

    @Test
    void testSettersAndGetters() {
        TicketHistoryResponseDto dto = new TicketHistoryResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setTicketId("val-ticketId");
        dto.setTicketNumber("val-ticketNumber");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setStatus("val-status");
        dto.setPriority("val-priority");
        dto.setCategory("val-category");
        dto.setChannel("val-channel");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setSatisfactionRating(99);
        dto.setFeedback("val-feedback");
        dto.setResolutionNotes("val-resolutionNotes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-ticketNumber", dto.getTicketNumber());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals(99, dto.getSatisfactionRating());
        assertEquals("val-feedback", dto.getFeedback());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketHistoryResponseDto dto1 = TicketHistoryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status("test-status")
            .priority("test-priority")
            .category("test-category")
            .channel("test-channel")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .satisfactionRating(42)
            .feedback("test-feedback")
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TicketHistoryResponseDto dto2 = TicketHistoryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status("test-status")
            .priority("test-priority")
            .category("test-category")
            .channel("test-channel")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .satisfactionRating(42)
            .feedback("test-feedback")
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketHistoryResponseDto dto = TicketHistoryResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status("test-status")
            .priority("test-priority")
            .category("test-category")
            .channel("test-channel")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .satisfactionRating(42)
            .feedback("test-feedback")
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
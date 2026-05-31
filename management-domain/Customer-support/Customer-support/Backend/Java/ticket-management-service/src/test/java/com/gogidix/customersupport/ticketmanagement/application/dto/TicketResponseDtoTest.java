package com.gogidix.customersupport.ticketmanagement.application.dto;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketResponseDto;
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
class TicketResponseDtoTest {

        @Test
    void testBuilder() {
        TicketResponseDto dto = TicketResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status(TicketResponseDto.TicketStatusDto.OPEN)
            .priority(TicketResponseDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .channel(TicketResponseDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .slaPolicyId("test-slaPolicyId")
            .slaDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreached(true)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .reopenedCount(42)
            .lastReopenedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ticketNumber", dto.getTicketNumber());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals(TicketResponseDto.TicketStatusDto.OPEN, dto.getStatus());
        assertEquals(TicketResponseDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-subCategory", dto.getSubCategory());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-customerPhone", dto.getCustomerPhone());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("test-assignedTeam", dto.getAssignedTeam());
        assertEquals(TicketResponseDto.TicketChannelDto.EMAIL, dto.getChannel());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-escalationReason", dto.getEscalationReason());
        assertEquals("test-slaPolicyId", dto.getSlaPolicyId());
        assertTrue(dto.getSlaBreached());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
        assertEquals("test-parentTicketId", dto.getParentTicketId());
        assertEquals(42, dto.getCustomerRating());
        assertEquals("test-customerFeedback", dto.getCustomerFeedback());
        assertEquals(42, dto.getReopenedCount());
    }

    @Test
    void testSettersAndGetters() {
        TicketResponseDto dto = new TicketResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setTicketNumber("val-ticketNumber");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setStatus(TicketResponseDto.TicketStatusDto.OPEN);
        dto.setPriority(TicketResponseDto.TicketPriorityDto.CRITICAL);
        dto.setCategory("val-category");
        dto.setSubCategory("val-subCategory");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setCustomerPhone("val-customerPhone");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setAssignedTeam("val-assignedTeam");
        dto.setChannel(TicketResponseDto.TicketChannelDto.EMAIL);
        dto.setSource("val-source");
        dto.setEscalationReason("val-escalationReason");
        dto.setSlaPolicyId("val-slaPolicyId");
        dto.setSlaBreached(true);
        dto.setResolutionNotes("val-resolutionNotes");
        dto.setParentTicketId("val-parentTicketId");
        dto.setCustomerRating(99);
        dto.setCustomerFeedback("val-customerFeedback");
        dto.setReopenedCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ticketNumber", dto.getTicketNumber());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(TicketResponseDto.TicketStatusDto.OPEN, dto.getStatus());
        assertEquals(TicketResponseDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-subCategory", dto.getSubCategory());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-customerPhone", dto.getCustomerPhone());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("val-assignedTeam", dto.getAssignedTeam());
        assertEquals(TicketResponseDto.TicketChannelDto.EMAIL, dto.getChannel());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-escalationReason", dto.getEscalationReason());
        assertEquals("val-slaPolicyId", dto.getSlaPolicyId());
        assertTrue(dto.getSlaBreached());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
        assertEquals("val-parentTicketId", dto.getParentTicketId());
        assertEquals(99, dto.getCustomerRating());
        assertEquals("val-customerFeedback", dto.getCustomerFeedback());
        assertEquals(99, dto.getReopenedCount());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketResponseDto dto1 = TicketResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status(TicketResponseDto.TicketStatusDto.OPEN)
            .priority(TicketResponseDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .channel(TicketResponseDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .slaPolicyId("test-slaPolicyId")
            .slaDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreached(true)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .reopenedCount(42)
            .lastReopenedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TicketResponseDto dto2 = TicketResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status(TicketResponseDto.TicketStatusDto.OPEN)
            .priority(TicketResponseDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .channel(TicketResponseDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .slaPolicyId("test-slaPolicyId")
            .slaDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreached(true)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .reopenedCount(42)
            .lastReopenedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketResponseDto dto = TicketResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketNumber("test-ticketNumber")
            .title("test-title")
            .description("test-description")
            .status(TicketResponseDto.TicketStatusDto.OPEN)
            .priority(TicketResponseDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .channel(TicketResponseDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .closedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .slaPolicyId("test-slaPolicyId")
            .slaDueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaBreached(true)
            .firstResponseAt(Instant.parse("2025-01-15T10:00:00Z"))
            .resolutionNotes("test-resolutionNotes")
            .attachments(Collections.emptyList())
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .reopenedCount(42)
            .lastReopenedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.customersupport.ticketmanagement.application.dto;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketUpdateRequestDto;
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
class TicketUpdateRequestDtoTest {

        @Test
    void testBuilder() {
        TicketUpdateRequestDto dto = TicketUpdateRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .status(TicketUpdateRequestDto.TicketStatusDto.OPEN)
            .priority(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .resolutionNotes("test-resolutionNotes")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals(TicketUpdateRequestDto.TicketStatusDto.OPEN, dto.getStatus());
        assertEquals(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-subCategory", dto.getSubCategory());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-customerPhone", dto.getCustomerPhone());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("test-assignedTeam", dto.getAssignedTeam());
        assertEquals("test-escalationReason", dto.getEscalationReason());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
        assertEquals("test-parentTicketId", dto.getParentTicketId());
        assertEquals(42, dto.getCustomerRating());
        assertEquals("test-customerFeedback", dto.getCustomerFeedback());
    }

    @Test
    void testSettersAndGetters() {
        TicketUpdateRequestDto dto = new TicketUpdateRequestDto();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setStatus(TicketUpdateRequestDto.TicketStatusDto.OPEN);
        dto.setPriority(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL);
        dto.setCategory("val-category");
        dto.setSubCategory("val-subCategory");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setCustomerPhone("val-customerPhone");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setAssignedTeam("val-assignedTeam");
        dto.setEscalationReason("val-escalationReason");
        dto.setResolutionNotes("val-resolutionNotes");
        dto.setParentTicketId("val-parentTicketId");
        dto.setCustomerRating(99);
        dto.setCustomerFeedback("val-customerFeedback");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(TicketUpdateRequestDto.TicketStatusDto.OPEN, dto.getStatus());
        assertEquals(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-subCategory", dto.getSubCategory());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-customerPhone", dto.getCustomerPhone());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("val-assignedTeam", dto.getAssignedTeam());
        assertEquals("val-escalationReason", dto.getEscalationReason());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
        assertEquals("val-parentTicketId", dto.getParentTicketId());
        assertEquals(99, dto.getCustomerRating());
        assertEquals("val-customerFeedback", dto.getCustomerFeedback());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketUpdateRequestDto dto1 = TicketUpdateRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .status(TicketUpdateRequestDto.TicketStatusDto.OPEN)
            .priority(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .resolutionNotes("test-resolutionNotes")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .build();
        TicketUpdateRequestDto dto2 = TicketUpdateRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .status(TicketUpdateRequestDto.TicketStatusDto.OPEN)
            .priority(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .resolutionNotes("test-resolutionNotes")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketUpdateRequestDto dto = TicketUpdateRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .status(TicketUpdateRequestDto.TicketStatusDto.OPEN)
            .priority(TicketUpdateRequestDto.TicketPriorityDto.CRITICAL)
            .category("test-category")
            .subCategory("test-subCategory")
            .tags(Collections.emptyList())
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .customerPhone("test-customerPhone")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationReason("test-escalationReason")
            .resolutionNotes("test-resolutionNotes")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .customerRating(42)
            .customerFeedback("test-customerFeedback")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
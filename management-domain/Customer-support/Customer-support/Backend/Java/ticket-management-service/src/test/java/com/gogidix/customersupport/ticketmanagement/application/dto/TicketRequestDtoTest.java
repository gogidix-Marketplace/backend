package com.gogidix.customersupport.ticketmanagement.application.dto;

import com.gogidix.customersupport.ticketmanagement.application.dto.TicketRequestDto;
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
class TicketRequestDtoTest {

        @Test
    void testBuilder() {
        TicketRequestDto dto = TicketRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .priority(TicketRequestDto.TicketPriorityDto.CRITICAL)
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
            .channel(TicketRequestDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaPolicyId("test-slaPolicyId")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals(TicketRequestDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-subCategory", dto.getSubCategory());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-customerPhone", dto.getCustomerPhone());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("test-assignedTeam", dto.getAssignedTeam());
        assertEquals(TicketRequestDto.TicketChannelDto.EMAIL, dto.getChannel());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-slaPolicyId", dto.getSlaPolicyId());
        assertEquals("test-parentTicketId", dto.getParentTicketId());
    }

    @Test
    void testSettersAndGetters() {
        TicketRequestDto dto = new TicketRequestDto();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setPriority(TicketRequestDto.TicketPriorityDto.CRITICAL);
        dto.setCategory("val-category");
        dto.setSubCategory("val-subCategory");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setCustomerPhone("val-customerPhone");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setAssignedTeam("val-assignedTeam");
        dto.setChannel(TicketRequestDto.TicketChannelDto.EMAIL);
        dto.setSource("val-source");
        dto.setSlaPolicyId("val-slaPolicyId");
        dto.setParentTicketId("val-parentTicketId");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals(TicketRequestDto.TicketPriorityDto.CRITICAL, dto.getPriority());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-subCategory", dto.getSubCategory());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-customerPhone", dto.getCustomerPhone());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("val-assignedTeam", dto.getAssignedTeam());
        assertEquals(TicketRequestDto.TicketChannelDto.EMAIL, dto.getChannel());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-slaPolicyId", dto.getSlaPolicyId());
        assertEquals("val-parentTicketId", dto.getParentTicketId());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketRequestDto dto1 = TicketRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .priority(TicketRequestDto.TicketPriorityDto.CRITICAL)
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
            .channel(TicketRequestDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaPolicyId("test-slaPolicyId")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .build();
        TicketRequestDto dto2 = TicketRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .priority(TicketRequestDto.TicketPriorityDto.CRITICAL)
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
            .channel(TicketRequestDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaPolicyId("test-slaPolicyId")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketRequestDto dto = TicketRequestDto.builder()
                        .title("test-title")
            .description("test-description")
            .priority(TicketRequestDto.TicketPriorityDto.CRITICAL)
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
            .channel(TicketRequestDto.TicketChannelDto.EMAIL)
            .source("test-source")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .slaPolicyId("test-slaPolicyId")
            .watchers(Collections.emptyList())
            .relatedTicketIds(Collections.emptyList())
            .parentTicketId("test-parentTicketId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
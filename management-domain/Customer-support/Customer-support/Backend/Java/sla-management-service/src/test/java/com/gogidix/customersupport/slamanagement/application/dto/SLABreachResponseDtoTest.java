package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLABreachResponseDto;
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
class SLABreachResponseDtoTest {

        @Test
    void testBuilder() {
        SLABreachResponseDto dto = SLABreachResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .slaPolicyId("test-slaPolicyId")
            .slaPolicyName("test-slaPolicyName")
            .breachType(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME)
            .breachDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .actualResponseTimeMinutes(42L)
            .actualResolutionTimeMinutes(42L)
            .targetTimeMinutes(42)
            .overdueByMinutes(42L)
            .severity("test-severity")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .customerId("test-customerId")
            .category("test-category")
            .priority("test-priority")
            .channel("test-channel")
            .isNotified(true)
            .notifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationTriggered(true)
            .escalationLevel(42)
            .resolutionNotes("test-resolutionNotes")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .impactScore(null)
            .preventiveActions("test-preventiveActions")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-ticketNumber", dto.getTicketNumber());
        assertEquals("test-slaPolicyId", dto.getSlaPolicyId());
        assertEquals("test-slaPolicyName", dto.getSlaPolicyName());
        assertEquals(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME, dto.getBreachType());
        assertEquals(42L, dto.getActualResponseTimeMinutes());
        assertEquals(42L, dto.getActualResolutionTimeMinutes());
        assertEquals(42, dto.getTargetTimeMinutes());
        assertEquals(42L, dto.getOverdueByMinutes());
        assertEquals("test-severity", dto.getSeverity());
        assertEquals("test-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("test-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("test-assignedTeam", dto.getAssignedTeam());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-channel", dto.getChannel());
        assertTrue(dto.getIsNotified());
        assertTrue(dto.getEscalationTriggered());
        assertEquals(42, dto.getEscalationLevel());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
        assertEquals("test-preventiveActions", dto.getPreventiveActions());
    }

    @Test
    void testSettersAndGetters() {
        SLABreachResponseDto dto = new SLABreachResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setTicketId("val-ticketId");
        dto.setTicketNumber("val-ticketNumber");
        dto.setSlaPolicyId("val-slaPolicyId");
        dto.setSlaPolicyName("val-slaPolicyName");
        dto.setBreachType(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME);
        dto.setTargetTimeMinutes(99);
        dto.setSeverity("val-severity");
        dto.setAssignedAgentId("val-assignedAgentId");
        dto.setAssignedAgentName("val-assignedAgentName");
        dto.setAssignedTeam("val-assignedTeam");
        dto.setCustomerId("val-customerId");
        dto.setCategory("val-category");
        dto.setPriority("val-priority");
        dto.setChannel("val-channel");
        dto.setIsNotified(true);
        dto.setEscalationTriggered(true);
        dto.setEscalationLevel(99);
        dto.setResolutionNotes("val-resolutionNotes");
        dto.setPreventiveActions("val-preventiveActions");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-ticketNumber", dto.getTicketNumber());
        assertEquals("val-slaPolicyId", dto.getSlaPolicyId());
        assertEquals("val-slaPolicyName", dto.getSlaPolicyName());
        assertEquals(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME, dto.getBreachType());
        assertEquals(99, dto.getTargetTimeMinutes());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-assignedAgentId", dto.getAssignedAgentId());
        assertEquals("val-assignedAgentName", dto.getAssignedAgentName());
        assertEquals("val-assignedTeam", dto.getAssignedTeam());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-channel", dto.getChannel());
        assertTrue(dto.getIsNotified());
        assertTrue(dto.getEscalationTriggered());
        assertEquals(99, dto.getEscalationLevel());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
        assertEquals("val-preventiveActions", dto.getPreventiveActions());
    }

    @Test
    void testEqualsAndHashCode() {
        SLABreachResponseDto dto1 = SLABreachResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .slaPolicyId("test-slaPolicyId")
            .slaPolicyName("test-slaPolicyName")
            .breachType(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME)
            .breachDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .actualResponseTimeMinutes(42L)
            .actualResolutionTimeMinutes(42L)
            .targetTimeMinutes(42)
            .overdueByMinutes(42L)
            .severity("test-severity")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .customerId("test-customerId")
            .category("test-category")
            .priority("test-priority")
            .channel("test-channel")
            .isNotified(true)
            .notifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationTriggered(true)
            .escalationLevel(42)
            .resolutionNotes("test-resolutionNotes")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .impactScore(null)
            .preventiveActions("test-preventiveActions")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        SLABreachResponseDto dto2 = SLABreachResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .slaPolicyId("test-slaPolicyId")
            .slaPolicyName("test-slaPolicyName")
            .breachType(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME)
            .breachDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .actualResponseTimeMinutes(42L)
            .actualResolutionTimeMinutes(42L)
            .targetTimeMinutes(42)
            .overdueByMinutes(42L)
            .severity("test-severity")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .customerId("test-customerId")
            .category("test-category")
            .priority("test-priority")
            .channel("test-channel")
            .isNotified(true)
            .notifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationTriggered(true)
            .escalationLevel(42)
            .resolutionNotes("test-resolutionNotes")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .impactScore(null)
            .preventiveActions("test-preventiveActions")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLABreachResponseDto dto = SLABreachResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .ticketNumber("test-ticketNumber")
            .slaPolicyId("test-slaPolicyId")
            .slaPolicyName("test-slaPolicyName")
            .breachType(SLABreachResponseDto.BreachTypeDto.RESPONSE_TIME)
            .breachDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .actualResponseTimeMinutes(42L)
            .actualResolutionTimeMinutes(42L)
            .targetTimeMinutes(42)
            .overdueByMinutes(42L)
            .severity("test-severity")
            .assignedAgentId("test-assignedAgentId")
            .assignedAgentName("test-assignedAgentName")
            .assignedTeam("test-assignedTeam")
            .customerId("test-customerId")
            .category("test-category")
            .priority("test-priority")
            .channel("test-channel")
            .isNotified(true)
            .notifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .escalationTriggered(true)
            .escalationLevel(42)
            .resolutionNotes("test-resolutionNotes")
            .resolvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .impactScore(null)
            .preventiveActions("test-preventiveActions")
            .metadata(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
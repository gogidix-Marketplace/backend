package com.gogidix.sales.leadmanagement.application.dto.response;

import com.gogidix.sales.leadmanagement.application.dto.response.LeadActivityResponseDto;
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
class LeadActivityResponseDtoTest {

        @Test
    void testBuilder() {
        LeadActivityResponseDto dto = LeadActivityResponseDto.builder()
                        .id("test-id")
            .activityId("test-activityId")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .activityType("test-activityType")
            .subject("test-subject")
            .description("test-description")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .priority("test-priority")
            .status("test-status")
            .durationMinutes(42)
            .outcome("test-outcome")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-activityId", dto.getActivityId());
        assertEquals("test-leadId", dto.getLeadId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-activityType", dto.getActivityType());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-createdByName", dto.getCreatedByName());
        assertEquals("test-priority", dto.getPriority());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getDurationMinutes());
        assertEquals("test-outcome", dto.getOutcome());
    }

    @Test
    void testSettersAndGetters() {
        LeadActivityResponseDto dto = new LeadActivityResponseDto();
        dto.setId("val-id");
        dto.setActivityId("val-activityId");
        dto.setLeadId("val-leadId");
        dto.setTenantId("val-tenantId");
        dto.setActivityType("val-activityType");
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        dto.setCreatedBy("val-createdBy");
        dto.setCreatedByName("val-createdByName");
        dto.setPriority("val-priority");
        dto.setStatus("val-status");
        dto.setDurationMinutes(99);
        dto.setOutcome("val-outcome");
        assertEquals("val-id", dto.getId());
        assertEquals("val-activityId", dto.getActivityId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-activityType", dto.getActivityType());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-priority", dto.getPriority());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getDurationMinutes());
        assertEquals("val-outcome", dto.getOutcome());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadActivityResponseDto dto1 = LeadActivityResponseDto.builder()
                        .id("test-id")
            .activityId("test-activityId")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .activityType("test-activityType")
            .subject("test-subject")
            .description("test-description")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .priority("test-priority")
            .status("test-status")
            .durationMinutes(42)
            .outcome("test-outcome")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        LeadActivityResponseDto dto2 = LeadActivityResponseDto.builder()
                        .id("test-id")
            .activityId("test-activityId")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .activityType("test-activityType")
            .subject("test-subject")
            .description("test-description")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .priority("test-priority")
            .status("test-status")
            .durationMinutes(42)
            .outcome("test-outcome")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadActivityResponseDto dto = LeadActivityResponseDto.builder()
                        .id("test-id")
            .activityId("test-activityId")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .activityType("test-activityType")
            .subject("test-subject")
            .description("test-description")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .priority("test-priority")
            .status("test-status")
            .durationMinutes(42)
            .outcome("test-outcome")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
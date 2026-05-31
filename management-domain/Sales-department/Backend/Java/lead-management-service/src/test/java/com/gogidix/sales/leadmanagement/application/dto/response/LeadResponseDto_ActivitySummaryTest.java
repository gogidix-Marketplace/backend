package com.gogidix.sales.leadmanagement.application.dto.response;

import com.gogidix.sales.leadmanagement.application.dto.response.LeadResponseDto;
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
class LeadResponseDto_ActivitySummaryTest {

        @Test
    void testBuilder() {
        LeadResponseDto.ActivitySummary dto = LeadResponseDto.ActivitySummary.builder()
                        .activityId("test-activityId")
            .activityType("test-activityType")
            .subject("test-subject")
            .status("test-status")
            .priority("test-priority")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-activityId", dto.getActivityId());
        assertEquals("test-activityType", dto.getActivityType());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-priority", dto.getPriority());
    }

    @Test
    void testSettersAndGetters() {
        LeadResponseDto.ActivitySummary dto = new LeadResponseDto.ActivitySummary();
        dto.setActivityId("val-activityId");
        dto.setActivityType("val-activityType");
        dto.setSubject("val-subject");
        dto.setStatus("val-status");
        dto.setPriority("val-priority");
        assertEquals("val-activityId", dto.getActivityId());
        assertEquals("val-activityType", dto.getActivityType());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-priority", dto.getPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadResponseDto.ActivitySummary dto1 = LeadResponseDto.ActivitySummary.builder()
                        .activityId("test-activityId")
            .activityType("test-activityType")
            .subject("test-subject")
            .status("test-status")
            .priority("test-priority")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        LeadResponseDto.ActivitySummary dto2 = LeadResponseDto.ActivitySummary.builder()
                        .activityId("test-activityId")
            .activityType("test-activityType")
            .subject("test-subject")
            .status("test-status")
            .priority("test-priority")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadResponseDto.ActivitySummary dto = LeadResponseDto.ActivitySummary.builder()
                        .activityId("test-activityId")
            .activityType("test-activityType")
            .subject("test-subject")
            .status("test-status")
            .priority("test-priority")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
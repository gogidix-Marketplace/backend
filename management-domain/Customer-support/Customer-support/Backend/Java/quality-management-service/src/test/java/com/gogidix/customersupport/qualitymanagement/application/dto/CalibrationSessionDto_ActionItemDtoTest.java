package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.CalibrationSessionDto;
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
class CalibrationSessionDto_ActionItemDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.ActionItemDto dto = CalibrationSessionDto.ActionItemDto.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status("test-status")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-actionItemId", dto.getActionItemId());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedToName", dto.getAssignedToName());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.ActionItemDto dto = new CalibrationSessionDto.ActionItemDto();
        dto.setActionItemId("val-actionItemId");
        dto.setDescription("val-description");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setStatus("val-status");
        dto.setNotes("val-notes");
        assertEquals("val-actionItemId", dto.getActionItemId());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.ActionItemDto dto1 = CalibrationSessionDto.ActionItemDto.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status("test-status")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        CalibrationSessionDto.ActionItemDto dto2 = CalibrationSessionDto.ActionItemDto.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status("test-status")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.ActionItemDto dto = CalibrationSessionDto.ActionItemDto.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status("test-status")
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
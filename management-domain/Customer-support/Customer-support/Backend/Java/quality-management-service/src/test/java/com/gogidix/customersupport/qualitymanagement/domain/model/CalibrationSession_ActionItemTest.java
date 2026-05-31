package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.CalibrationSession;
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
class CalibrationSession_ActionItemTest {

        @Test
    void testBuilder() {
        CalibrationSession.ActionItem dto = CalibrationSession.ActionItem.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(CalibrationSession.ActionItem.ActionItemStatus.PENDING)
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-actionItemId", dto.getActionItemId());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedToName", dto.getAssignedToName());
        assertEquals(CalibrationSession.ActionItem.ActionItemStatus.PENDING, dto.getStatus());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSession.ActionItem dto = new CalibrationSession.ActionItem();
        dto.setActionItemId("val-actionItemId");
        dto.setDescription("val-description");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setStatus(CalibrationSession.ActionItem.ActionItemStatus.PENDING);
        dto.setNotes("val-notes");
        assertEquals("val-actionItemId", dto.getActionItemId());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals(CalibrationSession.ActionItem.ActionItemStatus.PENDING, dto.getStatus());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSession.ActionItem dto1 = CalibrationSession.ActionItem.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(CalibrationSession.ActionItem.ActionItemStatus.PENDING)
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        CalibrationSession.ActionItem dto2 = CalibrationSession.ActionItem.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(CalibrationSession.ActionItem.ActionItemStatus.PENDING)
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSession.ActionItem dto = CalibrationSession.ActionItem.builder()
                        .actionItemId("test-actionItemId")
            .description("test-description")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(CalibrationSession.ActionItem.ActionItemStatus.PENDING)
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
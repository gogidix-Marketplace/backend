package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.AgentQualityProfileDto;
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
class AgentQualityProfileDto_CoachingNoteDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.CoachingNoteDto dto = AgentQualityProfileDto.CoachingNoteDto.builder()
                        .noteId("test-noteId")
            .note("test-note")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .category("test-category")
            .isActionable(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-noteId", dto.getNoteId());
        assertEquals("test-note", dto.getNote());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-createdByName", dto.getCreatedByName());
        assertEquals("test-category", dto.getCategory());
        assertTrue(dto.getIsActionable());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.CoachingNoteDto dto = new AgentQualityProfileDto.CoachingNoteDto();
        dto.setNoteId("val-noteId");
        dto.setNote("val-note");
        dto.setCreatedBy("val-createdBy");
        dto.setCreatedByName("val-createdByName");
        dto.setCategory("val-category");
        dto.setIsActionable(true);
        assertEquals("val-noteId", dto.getNoteId());
        assertEquals("val-note", dto.getNote());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-category", dto.getCategory());
        assertTrue(dto.getIsActionable());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.CoachingNoteDto dto1 = AgentQualityProfileDto.CoachingNoteDto.builder()
                        .noteId("test-noteId")
            .note("test-note")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .category("test-category")
            .isActionable(true)
            .build();
        AgentQualityProfileDto.CoachingNoteDto dto2 = AgentQualityProfileDto.CoachingNoteDto.builder()
                        .noteId("test-noteId")
            .note("test-note")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .category("test-category")
            .isActionable(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.CoachingNoteDto dto = AgentQualityProfileDto.CoachingNoteDto.builder()
                        .noteId("test-noteId")
            .note("test-note")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .category("test-category")
            .isActionable(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
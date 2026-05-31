package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.AgentQualityProfile;
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
class AgentQualityProfile_CoachingNoteTest {

        @Test
    void testBuilder() {
        AgentQualityProfile.CoachingNote dto = AgentQualityProfile.CoachingNote.builder()
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
        AgentQualityProfile.CoachingNote dto = new AgentQualityProfile.CoachingNote();
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
        AgentQualityProfile.CoachingNote dto1 = AgentQualityProfile.CoachingNote.builder()
                        .noteId("test-noteId")
            .note("test-note")
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .category("test-category")
            .isActionable(true)
            .build();
        AgentQualityProfile.CoachingNote dto2 = AgentQualityProfile.CoachingNote.builder()
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
        AgentQualityProfile.CoachingNote dto = AgentQualityProfile.CoachingNote.builder()
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
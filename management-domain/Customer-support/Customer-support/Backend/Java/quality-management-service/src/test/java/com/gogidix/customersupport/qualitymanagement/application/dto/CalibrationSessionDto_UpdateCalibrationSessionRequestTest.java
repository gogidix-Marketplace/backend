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
class CalibrationSessionDto_UpdateCalibrationSessionRequestTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.UpdateCalibrationSessionRequest dto = CalibrationSessionDto.UpdateCalibrationSessionRequest.builder()
                        .sessionName("test-sessionName")
            .description("test-description")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .scorecardTemplateId("test-scorecardTemplateId")
            .calibrationReviews(Collections.emptyList())
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-sessionName", dto.getSessionName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-sessionStatus", dto.getSessionStatus());
        assertEquals("test-facilitatorId", dto.getFacilitatorId());
        assertEquals("test-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getFollowUpRequired());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.UpdateCalibrationSessionRequest dto = new CalibrationSessionDto.UpdateCalibrationSessionRequest();
        dto.setSessionName("val-sessionName");
        dto.setDescription("val-description");
        dto.setSessionStatus("val-sessionStatus");
        dto.setFacilitatorId("val-facilitatorId");
        dto.setScorecardTemplateId("val-scorecardTemplateId");
        dto.setNotes("val-notes");
        dto.setFollowUpRequired(true);
        assertEquals("val-sessionName", dto.getSessionName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-sessionStatus", dto.getSessionStatus());
        assertEquals("val-facilitatorId", dto.getFacilitatorId());
        assertEquals("val-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getFollowUpRequired());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.UpdateCalibrationSessionRequest dto1 = CalibrationSessionDto.UpdateCalibrationSessionRequest.builder()
                        .sessionName("test-sessionName")
            .description("test-description")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .scorecardTemplateId("test-scorecardTemplateId")
            .calibrationReviews(Collections.emptyList())
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CalibrationSessionDto.UpdateCalibrationSessionRequest dto2 = CalibrationSessionDto.UpdateCalibrationSessionRequest.builder()
                        .sessionName("test-sessionName")
            .description("test-description")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .scorecardTemplateId("test-scorecardTemplateId")
            .calibrationReviews(Collections.emptyList())
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.UpdateCalibrationSessionRequest dto = CalibrationSessionDto.UpdateCalibrationSessionRequest.builder()
                        .sessionName("test-sessionName")
            .description("test-description")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .scorecardTemplateId("test-scorecardTemplateId")
            .calibrationReviews(Collections.emptyList())
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
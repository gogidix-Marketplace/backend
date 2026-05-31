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
class CalibrationSessionDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto dto = CalibrationSessionDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42)
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .calibrationReviews(Collections.emptyList())
            .targetInteractionsCount(42)
            .completedInteractionsCount(42)
            .averageScoreVariance(null)
            .maxScoreVariance(null)
            .interRaterReliability(null)
            .calibrationScore(null)
            .calibrationPassed(true)
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-sessionId", dto.getSessionId());
        assertEquals("test-sessionName", dto.getSessionName());
        assertEquals("test-sessionCode", dto.getSessionCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-sessionType", dto.getSessionType());
        assertEquals("test-sessionStatus", dto.getSessionStatus());
        assertEquals("test-facilitatorId", dto.getFacilitatorId());
        assertEquals("test-facilitatorName", dto.getFacilitatorName());
        assertEquals(42, dto.getDurationMinutes());
        assertEquals(42, dto.getMinParticipants());
        assertEquals(42, dto.getMaxParticipants());
        assertEquals("test-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("test-scorecardTemplateName", dto.getScorecardTemplateName());
        assertEquals(42, dto.getTargetInteractionsCount());
        assertEquals(42, dto.getCompletedInteractionsCount());
        assertTrue(dto.getCalibrationPassed());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getFollowUpRequired());
        assertEquals("test-location", dto.getLocation());
        assertTrue(dto.getIsVirtual());
        assertEquals("test-meetingLink", dto.getMeetingLink());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto dto = new CalibrationSessionDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setSessionId("val-sessionId");
        dto.setSessionName("val-sessionName");
        dto.setSessionCode("val-sessionCode");
        dto.setDescription("val-description");
        dto.setSessionType("val-sessionType");
        dto.setSessionStatus("val-sessionStatus");
        dto.setFacilitatorId("val-facilitatorId");
        dto.setFacilitatorName("val-facilitatorName");
        dto.setDurationMinutes(99);
        dto.setMinParticipants(99);
        dto.setMaxParticipants(99);
        dto.setScorecardTemplateId("val-scorecardTemplateId");
        dto.setScorecardTemplateName("val-scorecardTemplateName");
        dto.setTargetInteractionsCount(99);
        dto.setCompletedInteractionsCount(99);
        dto.setCalibrationPassed(true);
        dto.setNotes("val-notes");
        dto.setFollowUpRequired(true);
        dto.setLocation("val-location");
        dto.setIsVirtual(true);
        dto.setMeetingLink("val-meetingLink");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-sessionId", dto.getSessionId());
        assertEquals("val-sessionName", dto.getSessionName());
        assertEquals("val-sessionCode", dto.getSessionCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-sessionType", dto.getSessionType());
        assertEquals("val-sessionStatus", dto.getSessionStatus());
        assertEquals("val-facilitatorId", dto.getFacilitatorId());
        assertEquals("val-facilitatorName", dto.getFacilitatorName());
        assertEquals(99, dto.getDurationMinutes());
        assertEquals(99, dto.getMinParticipants());
        assertEquals(99, dto.getMaxParticipants());
        assertEquals("val-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("val-scorecardTemplateName", dto.getScorecardTemplateName());
        assertEquals(99, dto.getTargetInteractionsCount());
        assertEquals(99, dto.getCompletedInteractionsCount());
        assertTrue(dto.getCalibrationPassed());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getFollowUpRequired());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getIsVirtual());
        assertEquals("val-meetingLink", dto.getMeetingLink());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto dto1 = CalibrationSessionDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42)
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .calibrationReviews(Collections.emptyList())
            .targetInteractionsCount(42)
            .completedInteractionsCount(42)
            .averageScoreVariance(null)
            .maxScoreVariance(null)
            .interRaterReliability(null)
            .calibrationScore(null)
            .calibrationPassed(true)
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CalibrationSessionDto dto2 = CalibrationSessionDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42)
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .calibrationReviews(Collections.emptyList())
            .targetInteractionsCount(42)
            .completedInteractionsCount(42)
            .averageScoreVariance(null)
            .maxScoreVariance(null)
            .interRaterReliability(null)
            .calibrationScore(null)
            .calibrationPassed(true)
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto dto = CalibrationSessionDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualStartDate(Instant.parse("2025-01-15T10:00:00Z"))
            .actualEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .durationMinutes(42)
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .calibrationReviews(Collections.emptyList())
            .targetInteractionsCount(42)
            .completedInteractionsCount(42)
            .averageScoreVariance(null)
            .maxScoreVariance(null)
            .interRaterReliability(null)
            .calibrationScore(null)
            .calibrationPassed(true)
            .calibrationThreshold(null)
            .findings(Collections.emptyList())
            .actionItems(Collections.emptyList())
            .notes("test-notes")
            .followUpRequired(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
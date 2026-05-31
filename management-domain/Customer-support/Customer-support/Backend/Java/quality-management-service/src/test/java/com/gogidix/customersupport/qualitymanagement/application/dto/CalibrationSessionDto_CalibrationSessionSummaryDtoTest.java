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
class CalibrationSessionDto_CalibrationSessionSummaryDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.CalibrationSessionSummaryDto dto = CalibrationSessionDto.CalibrationSessionSummaryDto.builder()
                        .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participantCount(42)
            .completedInteractionsCount(42)
            .calibrationScore(null)
            .calibrationPassed(true)
            .followUpRequired(true)
            .isOverdue(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-sessionId", dto.getSessionId());
        assertEquals("test-sessionName", dto.getSessionName());
        assertEquals("test-sessionType", dto.getSessionType());
        assertEquals("test-sessionStatus", dto.getSessionStatus());
        assertEquals("test-facilitatorName", dto.getFacilitatorName());
        assertEquals(42, dto.getParticipantCount());
        assertEquals(42, dto.getCompletedInteractionsCount());
        assertTrue(dto.getCalibrationPassed());
        assertTrue(dto.getFollowUpRequired());
        assertTrue(dto.getIsOverdue());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.CalibrationSessionSummaryDto dto = new CalibrationSessionDto.CalibrationSessionSummaryDto();
        dto.setSessionId("val-sessionId");
        dto.setSessionName("val-sessionName");
        dto.setSessionType("val-sessionType");
        dto.setSessionStatus("val-sessionStatus");
        dto.setFacilitatorName("val-facilitatorName");
        dto.setParticipantCount(99);
        dto.setCompletedInteractionsCount(99);
        dto.setCalibrationPassed(true);
        dto.setFollowUpRequired(true);
        dto.setIsOverdue(true);
        assertEquals("val-sessionId", dto.getSessionId());
        assertEquals("val-sessionName", dto.getSessionName());
        assertEquals("val-sessionType", dto.getSessionType());
        assertEquals("val-sessionStatus", dto.getSessionStatus());
        assertEquals("val-facilitatorName", dto.getFacilitatorName());
        assertEquals(99, dto.getParticipantCount());
        assertEquals(99, dto.getCompletedInteractionsCount());
        assertTrue(dto.getCalibrationPassed());
        assertTrue(dto.getFollowUpRequired());
        assertTrue(dto.getIsOverdue());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.CalibrationSessionSummaryDto dto1 = CalibrationSessionDto.CalibrationSessionSummaryDto.builder()
                        .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participantCount(42)
            .completedInteractionsCount(42)
            .calibrationScore(null)
            .calibrationPassed(true)
            .followUpRequired(true)
            .isOverdue(true)
            .build();
        CalibrationSessionDto.CalibrationSessionSummaryDto dto2 = CalibrationSessionDto.CalibrationSessionSummaryDto.builder()
                        .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participantCount(42)
            .completedInteractionsCount(42)
            .calibrationScore(null)
            .calibrationPassed(true)
            .followUpRequired(true)
            .isOverdue(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.CalibrationSessionSummaryDto dto = CalibrationSessionDto.CalibrationSessionSummaryDto.builder()
                        .sessionId("test-sessionId")
            .sessionName("test-sessionName")
            .sessionType("test-sessionType")
            .sessionStatus("test-sessionStatus")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participantCount(42)
            .completedInteractionsCount(42)
            .calibrationScore(null)
            .calibrationPassed(true)
            .followUpRequired(true)
            .isOverdue(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
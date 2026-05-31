package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.QaReviewDto;
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
class QaReviewDto_UpdateQaReviewRequestTest {

        @Test
    void testBuilder() {
        QaReviewDto.UpdateQaReviewRequest dto = QaReviewDto.UpdateQaReviewRequest.builder()
                        .reviewStatus("test-reviewStatus")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .calibrationNotes("test-calibrationNotes")
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-reviewStatus", dto.getReviewStatus());
        assertEquals("test-overallComments", dto.getOverallComments());
        assertEquals("test-agentCoachingNotes", dto.getAgentCoachingNotes());
        assertTrue(dto.getRequiresEscalation());
        assertEquals("test-escalationReason", dto.getEscalationReason());
        assertEquals("test-escalatedTo", dto.getEscalatedTo());
        assertTrue(dto.getRequiresFollowUp());
        assertEquals("test-calibrationSessionId", dto.getCalibrationSessionId());
        assertEquals("test-calibrationNotes", dto.getCalibrationNotes());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewDto.UpdateQaReviewRequest dto = new QaReviewDto.UpdateQaReviewRequest();
        dto.setReviewStatus("val-reviewStatus");
        dto.setOverallComments("val-overallComments");
        dto.setAgentCoachingNotes("val-agentCoachingNotes");
        dto.setRequiresEscalation(true);
        dto.setEscalationReason("val-escalationReason");
        dto.setEscalatedTo("val-escalatedTo");
        dto.setRequiresFollowUp(true);
        dto.setCalibrationSessionId("val-calibrationSessionId");
        dto.setCalibrationNotes("val-calibrationNotes");
        assertEquals("val-reviewStatus", dto.getReviewStatus());
        assertEquals("val-overallComments", dto.getOverallComments());
        assertEquals("val-agentCoachingNotes", dto.getAgentCoachingNotes());
        assertTrue(dto.getRequiresEscalation());
        assertEquals("val-escalationReason", dto.getEscalationReason());
        assertEquals("val-escalatedTo", dto.getEscalatedTo());
        assertTrue(dto.getRequiresFollowUp());
        assertEquals("val-calibrationSessionId", dto.getCalibrationSessionId());
        assertEquals("val-calibrationNotes", dto.getCalibrationNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewDto.UpdateQaReviewRequest dto1 = QaReviewDto.UpdateQaReviewRequest.builder()
                        .reviewStatus("test-reviewStatus")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .calibrationNotes("test-calibrationNotes")
            .tags(Collections.emptyList())
            .build();
        QaReviewDto.UpdateQaReviewRequest dto2 = QaReviewDto.UpdateQaReviewRequest.builder()
                        .reviewStatus("test-reviewStatus")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .calibrationNotes("test-calibrationNotes")
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewDto.UpdateQaReviewRequest dto = QaReviewDto.UpdateQaReviewRequest.builder()
                        .reviewStatus("test-reviewStatus")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .calibrationNotes("test-calibrationNotes")
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
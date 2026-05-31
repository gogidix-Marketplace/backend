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
class QaReviewDtoTest {

        @Test
    void testBuilder() {
        QaReviewDto dto = QaReviewDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .totalScore(null)
            .maxScore(null)
            .percentageScore(null)
            .weightedScore(null)
            .passed(true)
            .criticalFailures(42)
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .isCalibrated(true)
            .calibrationNotes("test-calibrationNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .followUpCompleted(true)
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .externalReferenceId("test-externalReferenceId")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-reviewId", dto.getReviewId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-interactionId", dto.getInteractionId());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-reviewerId", dto.getReviewerId());
        assertEquals("test-reviewerName", dto.getReviewerName());
        assertEquals("test-reviewType", dto.getReviewType());
        assertEquals("test-reviewStatus", dto.getReviewStatus());
        assertEquals("test-channelType", dto.getChannelType());
        assertEquals("test-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("test-scorecardTemplateName", dto.getScorecardTemplateName());
        assertTrue(dto.getPassed());
        assertEquals(42, dto.getCriticalFailures());
        assertEquals("test-overallComments", dto.getOverallComments());
        assertEquals("test-agentCoachingNotes", dto.getAgentCoachingNotes());
        assertEquals("test-calibrationSessionId", dto.getCalibrationSessionId());
        assertTrue(dto.getIsCalibrated());
        assertEquals("test-calibrationNotes", dto.getCalibrationNotes());
        assertTrue(dto.getRequiresEscalation());
        assertEquals("test-escalationReason", dto.getEscalationReason());
        assertEquals("test-escalatedTo", dto.getEscalatedTo());
        assertTrue(dto.getRequiresFollowUp());
        assertTrue(dto.getFollowUpCompleted());
        assertEquals("test-reviewCycle", dto.getReviewCycle());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-externalReferenceId", dto.getExternalReferenceId());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewDto dto = new QaReviewDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setReviewId("val-reviewId");
        dto.setTicketId("val-ticketId");
        dto.setInteractionId("val-interactionId");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setReviewerId("val-reviewerId");
        dto.setReviewerName("val-reviewerName");
        dto.setReviewType("val-reviewType");
        dto.setReviewStatus("val-reviewStatus");
        dto.setChannelType("val-channelType");
        dto.setScorecardTemplateId("val-scorecardTemplateId");
        dto.setScorecardTemplateName("val-scorecardTemplateName");
        dto.setPassed(true);
        dto.setCriticalFailures(99);
        dto.setOverallComments("val-overallComments");
        dto.setAgentCoachingNotes("val-agentCoachingNotes");
        dto.setCalibrationSessionId("val-calibrationSessionId");
        dto.setIsCalibrated(true);
        dto.setCalibrationNotes("val-calibrationNotes");
        dto.setRequiresEscalation(true);
        dto.setEscalationReason("val-escalationReason");
        dto.setEscalatedTo("val-escalatedTo");
        dto.setRequiresFollowUp(true);
        dto.setFollowUpCompleted(true);
        dto.setReviewCycle("val-reviewCycle");
        dto.setBatchId("val-batchId");
        dto.setExternalReferenceId("val-externalReferenceId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reviewId", dto.getReviewId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-reviewerId", dto.getReviewerId());
        assertEquals("val-reviewerName", dto.getReviewerName());
        assertEquals("val-reviewType", dto.getReviewType());
        assertEquals("val-reviewStatus", dto.getReviewStatus());
        assertEquals("val-channelType", dto.getChannelType());
        assertEquals("val-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("val-scorecardTemplateName", dto.getScorecardTemplateName());
        assertTrue(dto.getPassed());
        assertEquals(99, dto.getCriticalFailures());
        assertEquals("val-overallComments", dto.getOverallComments());
        assertEquals("val-agentCoachingNotes", dto.getAgentCoachingNotes());
        assertEquals("val-calibrationSessionId", dto.getCalibrationSessionId());
        assertTrue(dto.getIsCalibrated());
        assertEquals("val-calibrationNotes", dto.getCalibrationNotes());
        assertTrue(dto.getRequiresEscalation());
        assertEquals("val-escalationReason", dto.getEscalationReason());
        assertEquals("val-escalatedTo", dto.getEscalatedTo());
        assertTrue(dto.getRequiresFollowUp());
        assertTrue(dto.getFollowUpCompleted());
        assertEquals("val-reviewCycle", dto.getReviewCycle());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-externalReferenceId", dto.getExternalReferenceId());
    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewDto dto1 = QaReviewDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .totalScore(null)
            .maxScore(null)
            .percentageScore(null)
            .weightedScore(null)
            .passed(true)
            .criticalFailures(42)
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .isCalibrated(true)
            .calibrationNotes("test-calibrationNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .followUpCompleted(true)
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .externalReferenceId("test-externalReferenceId")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        QaReviewDto dto2 = QaReviewDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .totalScore(null)
            .maxScore(null)
            .percentageScore(null)
            .weightedScore(null)
            .passed(true)
            .criticalFailures(42)
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .isCalibrated(true)
            .calibrationNotes("test-calibrationNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .followUpCompleted(true)
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .externalReferenceId("test-externalReferenceId")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewDto dto = QaReviewDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .scorecardTemplateName("test-scorecardTemplateName")
            .totalScore(null)
            .maxScore(null)
            .percentageScore(null)
            .weightedScore(null)
            .passed(true)
            .criticalFailures(42)
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .strengths(Collections.emptyList())
            .areasForImprovement(Collections.emptyList())
            .agentCoachingNotes("test-agentCoachingNotes")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .completedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .calibrationSessionId("test-calibrationSessionId")
            .isCalibrated(true)
            .calibrationNotes("test-calibrationNotes")
            .requiresEscalation(true)
            .escalationReason("test-escalationReason")
            .escalatedTo("test-escalatedTo")
            .escalatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .requiresFollowUp(true)
            .followUpDate(Instant.parse("2025-01-15T10:00:00Z"))
            .followUpCompleted(true)
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .externalReferenceId("test-externalReferenceId")
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
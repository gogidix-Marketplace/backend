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
class QaReviewDto_CreateQaReviewRequestTest {

        @Test
    void testBuilder() {
        QaReviewDto.CreateQaReviewRequest dto = QaReviewDto.CreateQaReviewRequest.builder()
                        .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-interactionId", dto.getInteractionId());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-reviewerId", dto.getReviewerId());
        assertEquals("test-reviewerName", dto.getReviewerName());
        assertEquals("test-reviewType", dto.getReviewType());
        assertEquals("test-channelType", dto.getChannelType());
        assertEquals("test-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("test-overallComments", dto.getOverallComments());
        assertEquals("test-reviewCycle", dto.getReviewCycle());
        assertEquals("test-batchId", dto.getBatchId());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewDto.CreateQaReviewRequest dto = new QaReviewDto.CreateQaReviewRequest();
        dto.setTenantId("val-tenantId");
        dto.setTicketId("val-ticketId");
        dto.setInteractionId("val-interactionId");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setReviewerId("val-reviewerId");
        dto.setReviewerName("val-reviewerName");
        dto.setReviewType("val-reviewType");
        dto.setChannelType("val-channelType");
        dto.setScorecardTemplateId("val-scorecardTemplateId");
        dto.setOverallComments("val-overallComments");
        dto.setReviewCycle("val-reviewCycle");
        dto.setBatchId("val-batchId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-reviewerId", dto.getReviewerId());
        assertEquals("val-reviewerName", dto.getReviewerName());
        assertEquals("val-reviewType", dto.getReviewType());
        assertEquals("val-channelType", dto.getChannelType());
        assertEquals("val-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals("val-overallComments", dto.getOverallComments());
        assertEquals("val-reviewCycle", dto.getReviewCycle());
        assertEquals("val-batchId", dto.getBatchId());
    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewDto.CreateQaReviewRequest dto1 = QaReviewDto.CreateQaReviewRequest.builder()
                        .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .tags(Collections.emptyList())
            .build();
        QaReviewDto.CreateQaReviewRequest dto2 = QaReviewDto.CreateQaReviewRequest.builder()
                        .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewDto.CreateQaReviewRequest dto = QaReviewDto.CreateQaReviewRequest.builder()
                        .tenantId("test-tenantId")
            .ticketId("test-ticketId")
            .interactionId("test-interactionId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerId("test-reviewerId")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .channelType("test-channelType")
            .scorecardTemplateId("test-scorecardTemplateId")
            .criteriaScores(Collections.emptyList())
            .overallComments("test-overallComments")
            .interactionDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .reviewCycle("test-reviewCycle")
            .batchId("test-batchId")
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
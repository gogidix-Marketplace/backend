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
class AgentQualityProfileDto_AgentQualityProfileSummaryDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.AgentQualityProfileSummaryDto dto = AgentQualityProfileDto.AgentQualityProfileSummaryDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .totalReviews(42L)
            .passRatePercentage(null)
            .qualityRiskLevel("test-qualityRiskLevel")
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-teamName", dto.getTeamName());
        assertEquals("test-overallQualityRank", dto.getOverallQualityRank());
        assertEquals("test-qualityTrend", dto.getQualityTrend());
        assertEquals(42L, dto.getTotalReviews());
        assertEquals("test-qualityRiskLevel", dto.getQualityRiskLevel());
        assertTrue(dto.getCoachingRequired());
        assertEquals("test-coachingPriority", dto.getCoachingPriority());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.AgentQualityProfileSummaryDto dto = new AgentQualityProfileDto.AgentQualityProfileSummaryDto();
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setTeamName("val-teamName");
        dto.setOverallQualityRank("val-overallQualityRank");
        dto.setQualityTrend("val-qualityTrend");
        dto.setQualityRiskLevel("val-qualityRiskLevel");
        dto.setCoachingRequired(true);
        dto.setCoachingPriority("val-coachingPriority");
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-teamName", dto.getTeamName());
        assertEquals("val-overallQualityRank", dto.getOverallQualityRank());
        assertEquals("val-qualityTrend", dto.getQualityTrend());
        assertEquals("val-qualityRiskLevel", dto.getQualityRiskLevel());
        assertTrue(dto.getCoachingRequired());
        assertEquals("val-coachingPriority", dto.getCoachingPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.AgentQualityProfileSummaryDto dto1 = AgentQualityProfileDto.AgentQualityProfileSummaryDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .totalReviews(42L)
            .passRatePercentage(null)
            .qualityRiskLevel("test-qualityRiskLevel")
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AgentQualityProfileDto.AgentQualityProfileSummaryDto dto2 = AgentQualityProfileDto.AgentQualityProfileSummaryDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .totalReviews(42L)
            .passRatePercentage(null)
            .qualityRiskLevel("test-qualityRiskLevel")
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.AgentQualityProfileSummaryDto dto = AgentQualityProfileDto.AgentQualityProfileSummaryDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .totalReviews(42L)
            .passRatePercentage(null)
            .qualityRiskLevel("test-qualityRiskLevel")
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
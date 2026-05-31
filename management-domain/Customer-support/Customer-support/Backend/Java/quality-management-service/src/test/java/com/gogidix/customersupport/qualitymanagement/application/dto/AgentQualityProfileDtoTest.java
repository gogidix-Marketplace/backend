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
class AgentQualityProfileDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto dto = AgentQualityProfileDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .managerId("test-managerId")
            .managerName("test-managerName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .rankInTeam(42)
            .percentileInTeam(null)
            .totalReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .passRatePercentage(null)
            .averageScore(null)
            .highestScore(null)
            .lowestScore(null)
            .scoreStandardDeviation(null)
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReviewScore(null)
            .lastReviewPassed(true)
            .currentStreak(42)
            .currentStreakType("test-currentStreakType")
            .longestPassingStreak(42)
            .longestFailingStreak(42)
            .monthlyAverageScore(null)
            .monthlyReviewsCount(42L)
            .monthlyPassRate(null)
            .quarterlyAverageScore(null)
            .quarterlyReviewsCount(42L)
            .quarterlyPassRate(null)
            .yearlyAverageScore(null)
            .yearlyReviewsCount(42L)
            .yearlyPassRate(null)
            .categoryPerformance(Collections.emptyList())
            .strengthAreas(Collections.emptyList())
            .improvementAreas(Collections.emptyList())
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .coachingNotes(Collections.emptyList())
            .developmentPlanId("test-developmentPlanId")
            .certificationsEarned(Collections.emptyList())
            .calibrationSessionsParticipated(42L)
            .calibrationAverageVariance(null)
            .calibrationComplianceScore(null)
            .channelPerformance(Collections.emptyMap())
            .scoreHistory(Collections.emptyList())
            .qualityGoal(null)
            .goalProgressPercentage(null)
            .goalTargetDate(Instant.parse("2025-01-15T10:00:00Z"))
            .goalAchieved(true)
            .qualityRiskLevel("test-qualityRiskLevel")
            .riskFactors(Collections.emptyList())
            .profileLastCalculated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-agentEmail", dto.getAgentEmail());
        assertEquals("test-teamId", dto.getTeamId());
        assertEquals("test-teamName", dto.getTeamName());
        assertEquals("test-managerId", dto.getManagerId());
        assertEquals("test-managerName", dto.getManagerName());
        assertEquals("test-overallQualityRank", dto.getOverallQualityRank());
        assertEquals("test-qualityTrend", dto.getQualityTrend());
        assertEquals(42, dto.getRankInTeam());
        assertEquals(42L, dto.getTotalReviews());
        assertEquals(42L, dto.getPassedReviews());
        assertEquals(42L, dto.getFailedReviews());
        assertTrue(dto.getLastReviewPassed());
        assertEquals(42, dto.getCurrentStreak());
        assertEquals("test-currentStreakType", dto.getCurrentStreakType());
        assertEquals(42, dto.getLongestPassingStreak());
        assertEquals(42, dto.getLongestFailingStreak());
        assertEquals(42L, dto.getMonthlyReviewsCount());
        assertEquals(42L, dto.getQuarterlyReviewsCount());
        assertEquals(42L, dto.getYearlyReviewsCount());
        assertTrue(dto.getCoachingRequired());
        assertEquals("test-coachingPriority", dto.getCoachingPriority());
        assertEquals("test-developmentPlanId", dto.getDevelopmentPlanId());
        assertEquals(42L, dto.getCalibrationSessionsParticipated());
        assertTrue(dto.getGoalAchieved());
        assertEquals("test-qualityRiskLevel", dto.getQualityRiskLevel());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto dto = new AgentQualityProfileDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setAgentEmail("val-agentEmail");
        dto.setTeamId("val-teamId");
        dto.setTeamName("val-teamName");
        dto.setManagerId("val-managerId");
        dto.setManagerName("val-managerName");
        dto.setOverallQualityRank("val-overallQualityRank");
        dto.setQualityTrend("val-qualityTrend");
        dto.setRankInTeam(99);
        dto.setLastReviewPassed(true);
        dto.setCurrentStreak(99);
        dto.setCurrentStreakType("val-currentStreakType");
        dto.setLongestPassingStreak(99);
        dto.setLongestFailingStreak(99);
        dto.setCoachingRequired(true);
        dto.setCoachingPriority("val-coachingPriority");
        dto.setDevelopmentPlanId("val-developmentPlanId");
        dto.setGoalAchieved(true);
        dto.setQualityRiskLevel("val-qualityRiskLevel");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-agentEmail", dto.getAgentEmail());
        assertEquals("val-teamId", dto.getTeamId());
        assertEquals("val-teamName", dto.getTeamName());
        assertEquals("val-managerId", dto.getManagerId());
        assertEquals("val-managerName", dto.getManagerName());
        assertEquals("val-overallQualityRank", dto.getOverallQualityRank());
        assertEquals("val-qualityTrend", dto.getQualityTrend());
        assertEquals(99, dto.getRankInTeam());
        assertTrue(dto.getLastReviewPassed());
        assertEquals(99, dto.getCurrentStreak());
        assertEquals("val-currentStreakType", dto.getCurrentStreakType());
        assertEquals(99, dto.getLongestPassingStreak());
        assertEquals(99, dto.getLongestFailingStreak());
        assertTrue(dto.getCoachingRequired());
        assertEquals("val-coachingPriority", dto.getCoachingPriority());
        assertEquals("val-developmentPlanId", dto.getDevelopmentPlanId());
        assertTrue(dto.getGoalAchieved());
        assertEquals("val-qualityRiskLevel", dto.getQualityRiskLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto dto1 = AgentQualityProfileDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .managerId("test-managerId")
            .managerName("test-managerName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .rankInTeam(42)
            .percentileInTeam(null)
            .totalReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .passRatePercentage(null)
            .averageScore(null)
            .highestScore(null)
            .lowestScore(null)
            .scoreStandardDeviation(null)
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReviewScore(null)
            .lastReviewPassed(true)
            .currentStreak(42)
            .currentStreakType("test-currentStreakType")
            .longestPassingStreak(42)
            .longestFailingStreak(42)
            .monthlyAverageScore(null)
            .monthlyReviewsCount(42L)
            .monthlyPassRate(null)
            .quarterlyAverageScore(null)
            .quarterlyReviewsCount(42L)
            .quarterlyPassRate(null)
            .yearlyAverageScore(null)
            .yearlyReviewsCount(42L)
            .yearlyPassRate(null)
            .categoryPerformance(Collections.emptyList())
            .strengthAreas(Collections.emptyList())
            .improvementAreas(Collections.emptyList())
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .coachingNotes(Collections.emptyList())
            .developmentPlanId("test-developmentPlanId")
            .certificationsEarned(Collections.emptyList())
            .calibrationSessionsParticipated(42L)
            .calibrationAverageVariance(null)
            .calibrationComplianceScore(null)
            .channelPerformance(Collections.emptyMap())
            .scoreHistory(Collections.emptyList())
            .qualityGoal(null)
            .goalProgressPercentage(null)
            .goalTargetDate(Instant.parse("2025-01-15T10:00:00Z"))
            .goalAchieved(true)
            .qualityRiskLevel("test-qualityRiskLevel")
            .riskFactors(Collections.emptyList())
            .profileLastCalculated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AgentQualityProfileDto dto2 = AgentQualityProfileDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .managerId("test-managerId")
            .managerName("test-managerName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .rankInTeam(42)
            .percentileInTeam(null)
            .totalReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .passRatePercentage(null)
            .averageScore(null)
            .highestScore(null)
            .lowestScore(null)
            .scoreStandardDeviation(null)
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReviewScore(null)
            .lastReviewPassed(true)
            .currentStreak(42)
            .currentStreakType("test-currentStreakType")
            .longestPassingStreak(42)
            .longestFailingStreak(42)
            .monthlyAverageScore(null)
            .monthlyReviewsCount(42L)
            .monthlyPassRate(null)
            .quarterlyAverageScore(null)
            .quarterlyReviewsCount(42L)
            .quarterlyPassRate(null)
            .yearlyAverageScore(null)
            .yearlyReviewsCount(42L)
            .yearlyPassRate(null)
            .categoryPerformance(Collections.emptyList())
            .strengthAreas(Collections.emptyList())
            .improvementAreas(Collections.emptyList())
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .coachingNotes(Collections.emptyList())
            .developmentPlanId("test-developmentPlanId")
            .certificationsEarned(Collections.emptyList())
            .calibrationSessionsParticipated(42L)
            .calibrationAverageVariance(null)
            .calibrationComplianceScore(null)
            .channelPerformance(Collections.emptyMap())
            .scoreHistory(Collections.emptyList())
            .qualityGoal(null)
            .goalProgressPercentage(null)
            .goalTargetDate(Instant.parse("2025-01-15T10:00:00Z"))
            .goalAchieved(true)
            .qualityRiskLevel("test-qualityRiskLevel")
            .riskFactors(Collections.emptyList())
            .profileLastCalculated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto dto = AgentQualityProfileDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .agentEmail("test-agentEmail")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .managerId("test-managerId")
            .managerName("test-managerName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .qualityTrend("test-qualityTrend")
            .rankInTeam(42)
            .percentileInTeam(null)
            .totalReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .passRatePercentage(null)
            .averageScore(null)
            .highestScore(null)
            .lowestScore(null)
            .scoreStandardDeviation(null)
            .lastReviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastReviewScore(null)
            .lastReviewPassed(true)
            .currentStreak(42)
            .currentStreakType("test-currentStreakType")
            .longestPassingStreak(42)
            .longestFailingStreak(42)
            .monthlyAverageScore(null)
            .monthlyReviewsCount(42L)
            .monthlyPassRate(null)
            .quarterlyAverageScore(null)
            .quarterlyReviewsCount(42L)
            .quarterlyPassRate(null)
            .yearlyAverageScore(null)
            .yearlyReviewsCount(42L)
            .yearlyPassRate(null)
            .categoryPerformance(Collections.emptyList())
            .strengthAreas(Collections.emptyList())
            .improvementAreas(Collections.emptyList())
            .coachingRequired(true)
            .coachingPriority("test-coachingPriority")
            .coachingNotes(Collections.emptyList())
            .developmentPlanId("test-developmentPlanId")
            .certificationsEarned(Collections.emptyList())
            .calibrationSessionsParticipated(42L)
            .calibrationAverageVariance(null)
            .calibrationComplianceScore(null)
            .channelPerformance(Collections.emptyMap())
            .scoreHistory(Collections.emptyList())
            .qualityGoal(null)
            .goalProgressPercentage(null)
            .goalTargetDate(Instant.parse("2025-01-15T10:00:00Z"))
            .goalAchieved(true)
            .qualityRiskLevel("test-qualityRiskLevel")
            .riskFactors(Collections.emptyList())
            .profileLastCalculated(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
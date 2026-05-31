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
class AgentQualityProfileDto_QualityMetricsDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.QualityMetricsDto dto = AgentQualityProfileDto.QualityMetricsDto.builder()
                        .totalAgents(42L)
            .agentsReviewed(42L)
            .averageQualityScore(null)
            .overallPassRate(null)
            .exemplaryAgents(42L)
            .agentsNeedingImprovement(42L)
            .criticalRiskAgents(42L)
            .coachingRequiredAgents(42L)
            .averageCalibrationScore(null)
            .totalCalibrationSessions(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalAgents());
        assertEquals(42L, dto.getAgentsReviewed());
        assertEquals(42L, dto.getExemplaryAgents());
        assertEquals(42L, dto.getAgentsNeedingImprovement());
        assertEquals(42L, dto.getCriticalRiskAgents());
        assertEquals(42L, dto.getCoachingRequiredAgents());
        assertEquals(42L, dto.getTotalCalibrationSessions());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.QualityMetricsDto dto = new AgentQualityProfileDto.QualityMetricsDto();


    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.QualityMetricsDto dto1 = AgentQualityProfileDto.QualityMetricsDto.builder()
                        .totalAgents(42L)
            .agentsReviewed(42L)
            .averageQualityScore(null)
            .overallPassRate(null)
            .exemplaryAgents(42L)
            .agentsNeedingImprovement(42L)
            .criticalRiskAgents(42L)
            .coachingRequiredAgents(42L)
            .averageCalibrationScore(null)
            .totalCalibrationSessions(42L)
            .build();
        AgentQualityProfileDto.QualityMetricsDto dto2 = AgentQualityProfileDto.QualityMetricsDto.builder()
                        .totalAgents(42L)
            .agentsReviewed(42L)
            .averageQualityScore(null)
            .overallPassRate(null)
            .exemplaryAgents(42L)
            .agentsNeedingImprovement(42L)
            .criticalRiskAgents(42L)
            .coachingRequiredAgents(42L)
            .averageCalibrationScore(null)
            .totalCalibrationSessions(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.QualityMetricsDto dto = AgentQualityProfileDto.QualityMetricsDto.builder()
                        .totalAgents(42L)
            .agentsReviewed(42L)
            .averageQualityScore(null)
            .overallPassRate(null)
            .exemplaryAgents(42L)
            .agentsNeedingImprovement(42L)
            .criticalRiskAgents(42L)
            .coachingRequiredAgents(42L)
            .averageCalibrationScore(null)
            .totalCalibrationSessions(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
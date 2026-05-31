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
class AgentQualityProfileDto_QualityLeaderboardDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.QualityLeaderboardDto dto = AgentQualityProfileDto.QualityLeaderboardDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .totalReviews(42L)
            .passRatePercentage(null)
            .rank(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-teamName", dto.getTeamName());
        assertEquals("test-overallQualityRank", dto.getOverallQualityRank());
        assertEquals(42L, dto.getTotalReviews());
        assertEquals(42, dto.getRank());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.QualityLeaderboardDto dto = new AgentQualityProfileDto.QualityLeaderboardDto();
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setTeamName("val-teamName");
        dto.setOverallQualityRank("val-overallQualityRank");
        dto.setRank(99);
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-teamName", dto.getTeamName());
        assertEquals("val-overallQualityRank", dto.getOverallQualityRank());
        assertEquals(99, dto.getRank());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.QualityLeaderboardDto dto1 = AgentQualityProfileDto.QualityLeaderboardDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .totalReviews(42L)
            .passRatePercentage(null)
            .rank(42)
            .build();
        AgentQualityProfileDto.QualityLeaderboardDto dto2 = AgentQualityProfileDto.QualityLeaderboardDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .totalReviews(42L)
            .passRatePercentage(null)
            .rank(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.QualityLeaderboardDto dto = AgentQualityProfileDto.QualityLeaderboardDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .teamName("test-teamName")
            .overallQualityScore(null)
            .overallQualityRank("test-overallQualityRank")
            .totalReviews(42L)
            .passRatePercentage(null)
            .rank(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
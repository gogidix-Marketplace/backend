package com.gogidix.customersupport.feedback.application.dto.response;

import com.gogidix.customersupport.feedback.application.dto.response.NPSMetricResponseDto;
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
class NPSMetricResponseDtoTest {

        @Test
    void testBuilder() {
        NPSMetricResponseDto dto = NPSMetricResponseDto.builder()
                        .id("test-id")
            .metricId("test-metricId")
            .tenantId("test-tenantId")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .npsScore(42)
            .promotersCount(42)
            .promotersPercentage(null)
            .passivesCount(42)
            .passivesPercentage(null)
            .detractorsCount(42)
            .detractorsPercentage(null)
            .totalResponses(42)
            .averageScore(null)
            .countryCode("test-countryCode")
            .agentId("test-agentId")
            .teamId("test-teamId")
            .channel("test-channel")
            .previousNpsScore(42)
            .scoreChange(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-metricId", dto.getMetricId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(42, dto.getNpsScore());
        assertEquals(42, dto.getPromotersCount());
        assertEquals(42, dto.getPassivesCount());
        assertEquals(42, dto.getDetractorsCount());
        assertEquals(42, dto.getTotalResponses());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-teamId", dto.getTeamId());
        assertEquals("test-channel", dto.getChannel());
        assertEquals(42, dto.getPreviousNpsScore());
        assertEquals(42, dto.getScoreChange());
    }

    @Test
    void testSettersAndGetters() {
        NPSMetricResponseDto dto = new NPSMetricResponseDto();
        dto.setId("val-id");
        dto.setMetricId("val-metricId");
        dto.setTenantId("val-tenantId");
        dto.setNpsScore(99);
        dto.setPromotersCount(99);
        dto.setPassivesCount(99);
        dto.setDetractorsCount(99);
        dto.setTotalResponses(99);
        dto.setCountryCode("val-countryCode");
        dto.setAgentId("val-agentId");
        dto.setTeamId("val-teamId");
        dto.setChannel("val-channel");
        dto.setPreviousNpsScore(99);
        dto.setScoreChange(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-metricId", dto.getMetricId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getNpsScore());
        assertEquals(99, dto.getPromotersCount());
        assertEquals(99, dto.getPassivesCount());
        assertEquals(99, dto.getDetractorsCount());
        assertEquals(99, dto.getTotalResponses());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-teamId", dto.getTeamId());
        assertEquals("val-channel", dto.getChannel());
        assertEquals(99, dto.getPreviousNpsScore());
        assertEquals(99, dto.getScoreChange());
    }

    @Test
    void testEqualsAndHashCode() {
        NPSMetricResponseDto dto1 = NPSMetricResponseDto.builder()
                        .id("test-id")
            .metricId("test-metricId")
            .tenantId("test-tenantId")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .npsScore(42)
            .promotersCount(42)
            .promotersPercentage(null)
            .passivesCount(42)
            .passivesPercentage(null)
            .detractorsCount(42)
            .detractorsPercentage(null)
            .totalResponses(42)
            .averageScore(null)
            .countryCode("test-countryCode")
            .agentId("test-agentId")
            .teamId("test-teamId")
            .channel("test-channel")
            .previousNpsScore(42)
            .scoreChange(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NPSMetricResponseDto dto2 = NPSMetricResponseDto.builder()
                        .id("test-id")
            .metricId("test-metricId")
            .tenantId("test-tenantId")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .npsScore(42)
            .promotersCount(42)
            .promotersPercentage(null)
            .passivesCount(42)
            .passivesPercentage(null)
            .detractorsCount(42)
            .detractorsPercentage(null)
            .totalResponses(42)
            .averageScore(null)
            .countryCode("test-countryCode")
            .agentId("test-agentId")
            .teamId("test-teamId")
            .channel("test-channel")
            .previousNpsScore(42)
            .scoreChange(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NPSMetricResponseDto dto = NPSMetricResponseDto.builder()
                        .id("test-id")
            .metricId("test-metricId")
            .tenantId("test-tenantId")
            .periodStart(Instant.parse("2025-01-15T10:00:00Z"))
            .periodEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .npsScore(42)
            .promotersCount(42)
            .promotersPercentage(null)
            .passivesCount(42)
            .passivesPercentage(null)
            .detractorsCount(42)
            .detractorsPercentage(null)
            .totalResponses(42)
            .averageScore(null)
            .countryCode("test-countryCode")
            .agentId("test-agentId")
            .teamId("test-teamId")
            .channel("test-channel")
            .previousNpsScore(42)
            .scoreChange(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
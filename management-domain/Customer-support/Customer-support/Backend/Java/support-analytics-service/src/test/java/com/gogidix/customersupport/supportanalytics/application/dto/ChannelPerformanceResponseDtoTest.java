package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.ChannelPerformanceResponseDto;
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
class ChannelPerformanceResponseDtoTest {

        @Test
    void testBuilder() {
        ChannelPerformanceResponseDto dto = ChannelPerformanceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .channelType(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL)
            .metricDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .resolvedInteractions(42)
            .pendingInteractions(42)
            .averageResponseTimeSeconds(42L)
            .averageResolutionTimeSeconds(42L)
            .abandonmentRate(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .agentUtilization(null)
            .averageHandleTimeSeconds(42L)
            .totalHandleTimeSeconds(42L)
            .activeAgents(42)
            .resolutionRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL, dto.getChannelType());
        assertEquals(LocalDate.of(2025,1,15), dto.getMetricDate());
        assertEquals(42, dto.getTotalInteractions());
        assertEquals(42, dto.getResolvedInteractions());
        assertEquals(42, dto.getPendingInteractions());
        assertEquals(42L, dto.getAverageResponseTimeSeconds());
        assertEquals(42L, dto.getAverageResolutionTimeSeconds());
        assertEquals(42L, dto.getAverageHandleTimeSeconds());
        assertEquals(42L, dto.getTotalHandleTimeSeconds());
        assertEquals(42, dto.getActiveAgents());
    }

    @Test
    void testSettersAndGetters() {
        ChannelPerformanceResponseDto dto = new ChannelPerformanceResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setChannelType(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL);
        dto.setMetricDate(LocalDate.of(2025,6,1));
        dto.setTotalInteractions(99);
        dto.setResolvedInteractions(99);
        dto.setPendingInteractions(99);
        dto.setActiveAgents(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL, dto.getChannelType());
        assertEquals(LocalDate.of(2025,6,1), dto.getMetricDate());
        assertEquals(99, dto.getTotalInteractions());
        assertEquals(99, dto.getResolvedInteractions());
        assertEquals(99, dto.getPendingInteractions());
        assertEquals(99, dto.getActiveAgents());
    }

    @Test
    void testEqualsAndHashCode() {
        ChannelPerformanceResponseDto dto1 = ChannelPerformanceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .channelType(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL)
            .metricDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .resolvedInteractions(42)
            .pendingInteractions(42)
            .averageResponseTimeSeconds(42L)
            .averageResolutionTimeSeconds(42L)
            .abandonmentRate(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .agentUtilization(null)
            .averageHandleTimeSeconds(42L)
            .totalHandleTimeSeconds(42L)
            .activeAgents(42)
            .resolutionRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ChannelPerformanceResponseDto dto2 = ChannelPerformanceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .channelType(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL)
            .metricDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .resolvedInteractions(42)
            .pendingInteractions(42)
            .averageResponseTimeSeconds(42L)
            .averageResolutionTimeSeconds(42L)
            .abandonmentRate(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .agentUtilization(null)
            .averageHandleTimeSeconds(42L)
            .totalHandleTimeSeconds(42L)
            .activeAgents(42)
            .resolutionRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChannelPerformanceResponseDto dto = ChannelPerformanceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .channelType(ChannelPerformanceResponseDto.ChannelTypeDto.EMAIL)
            .metricDate(LocalDate.of(2025,1,15))
            .totalInteractions(42)
            .resolvedInteractions(42)
            .pendingInteractions(42)
            .averageResponseTimeSeconds(42L)
            .averageResolutionTimeSeconds(42L)
            .abandonmentRate(null)
            .customerSatisfactionScore(null)
            .firstContactResolutionRate(null)
            .peakHours(Collections.emptyMap())
            .agentUtilization(null)
            .averageHandleTimeSeconds(42L)
            .totalHandleTimeSeconds(42L)
            .activeAgents(42)
            .resolutionRate(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
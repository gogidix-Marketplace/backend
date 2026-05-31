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
class AgentQualityProfileDto_ChannelStatsDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.ChannelStatsDto dto = AgentQualityProfileDto.ChannelStatsDto.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals("test-channel", dto.getChannel());
        assertEquals(42L, dto.getReviewCount());
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.ChannelStatsDto dto = new AgentQualityProfileDto.ChannelStatsDto();
        dto.setChannel("val-channel");
        dto.setTrend("val-trend");
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.ChannelStatsDto dto1 = AgentQualityProfileDto.ChannelStatsDto.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend("test-trend")
            .build();
        AgentQualityProfileDto.ChannelStatsDto dto2 = AgentQualityProfileDto.ChannelStatsDto.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.ChannelStatsDto dto = AgentQualityProfileDto.ChannelStatsDto.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportRequestDto;
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
class AnalyticsReportRequestDto_ChannelPerformanceMetricDtoTest {

        @Test
    void testBuilder() {
        AnalyticsReportRequestDto.ChannelPerformanceMetricDto dto = AnalyticsReportRequestDto.ChannelPerformanceMetricDto.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-channel", dto.getChannel());
        assertEquals(42, dto.getTicketsReceived());
        assertEquals(42, dto.getTicketsResolved());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReportRequestDto.ChannelPerformanceMetricDto dto = new AnalyticsReportRequestDto.ChannelPerformanceMetricDto();
        dto.setChannel("val-channel");
        dto.setTicketsReceived(99);
        dto.setTicketsResolved(99);
        assertEquals("val-channel", dto.getChannel());
        assertEquals(99, dto.getTicketsReceived());
        assertEquals(99, dto.getTicketsResolved());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReportRequestDto.ChannelPerformanceMetricDto dto1 = AnalyticsReportRequestDto.ChannelPerformanceMetricDto.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        AnalyticsReportRequestDto.ChannelPerformanceMetricDto dto2 = AnalyticsReportRequestDto.ChannelPerformanceMetricDto.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReportRequestDto.ChannelPerformanceMetricDto dto = AnalyticsReportRequestDto.ChannelPerformanceMetricDto.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
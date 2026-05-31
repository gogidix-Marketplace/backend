package com.gogidix.customersupport.supportanalytics.application.dto;

import com.gogidix.customersupport.supportanalytics.application.dto.AnalyticsReportResponseDto;
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
class AnalyticsReportResponseDto_ChannelPerformanceMetricDtoTest {

        @Test
    void testBuilder() {
        AnalyticsReportResponseDto.ChannelPerformanceMetricDto dto = AnalyticsReportResponseDto.ChannelPerformanceMetricDto.builder()
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
        AnalyticsReportResponseDto.ChannelPerformanceMetricDto dto = new AnalyticsReportResponseDto.ChannelPerformanceMetricDto();
        dto.setChannel("val-channel");
        dto.setTicketsReceived(99);
        dto.setTicketsResolved(99);
        assertEquals("val-channel", dto.getChannel());
        assertEquals(99, dto.getTicketsReceived());
        assertEquals(99, dto.getTicketsResolved());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReportResponseDto.ChannelPerformanceMetricDto dto1 = AnalyticsReportResponseDto.ChannelPerformanceMetricDto.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        AnalyticsReportResponseDto.ChannelPerformanceMetricDto dto2 = AnalyticsReportResponseDto.ChannelPerformanceMetricDto.builder()
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
        AnalyticsReportResponseDto.ChannelPerformanceMetricDto dto = AnalyticsReportResponseDto.ChannelPerformanceMetricDto.builder()
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
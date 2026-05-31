package com.gogidix.customersupport.supportanalytics.domain.model;

import com.gogidix.customersupport.supportanalytics.domain.model.AnalyticsReport;
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
class AnalyticsReport_ChannelPerformanceMetricTest {

        @Test
    void testBuilder() {
        AnalyticsReport.ChannelPerformanceMetric dto = AnalyticsReport.ChannelPerformanceMetric.builder()
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
        AnalyticsReport.ChannelPerformanceMetric dto = new AnalyticsReport.ChannelPerformanceMetric();
        dto.setChannel("val-channel");
        dto.setTicketsReceived(99);
        dto.setTicketsResolved(99);
        assertEquals("val-channel", dto.getChannel());
        assertEquals(99, dto.getTicketsReceived());
        assertEquals(99, dto.getTicketsResolved());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReport.ChannelPerformanceMetric dto1 = AnalyticsReport.ChannelPerformanceMetric.builder()
                        .channel("test-channel")
            .ticketsReceived(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .satisfactionScore(null)
            .build();
        AnalyticsReport.ChannelPerformanceMetric dto2 = AnalyticsReport.ChannelPerformanceMetric.builder()
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
        AnalyticsReport.ChannelPerformanceMetric dto = AnalyticsReport.ChannelPerformanceMetric.builder()
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
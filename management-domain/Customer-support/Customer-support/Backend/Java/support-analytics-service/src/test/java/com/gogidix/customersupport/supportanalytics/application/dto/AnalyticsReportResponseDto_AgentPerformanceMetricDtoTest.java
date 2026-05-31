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
class AnalyticsReportResponseDto_AgentPerformanceMetricDtoTest {

        @Test
    void testBuilder() {
        AnalyticsReportResponseDto.AgentPerformanceMetricDto dto = AnalyticsReportResponseDto.AgentPerformanceMetricDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .ticketsHandled(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .averageResponseTime(null)
            .satisfactionScore(null)
            .escalations(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals(42, dto.getTicketsHandled());
        assertEquals(42, dto.getTicketsResolved());
        assertEquals(42, dto.getEscalations());
    }

    @Test
    void testSettersAndGetters() {
        AnalyticsReportResponseDto.AgentPerformanceMetricDto dto = new AnalyticsReportResponseDto.AgentPerformanceMetricDto();
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setTicketsHandled(99);
        dto.setTicketsResolved(99);
        dto.setEscalations(99);
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals(99, dto.getTicketsHandled());
        assertEquals(99, dto.getTicketsResolved());
        assertEquals(99, dto.getEscalations());
    }

    @Test
    void testEqualsAndHashCode() {
        AnalyticsReportResponseDto.AgentPerformanceMetricDto dto1 = AnalyticsReportResponseDto.AgentPerformanceMetricDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .ticketsHandled(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .averageResponseTime(null)
            .satisfactionScore(null)
            .escalations(42)
            .build();
        AnalyticsReportResponseDto.AgentPerformanceMetricDto dto2 = AnalyticsReportResponseDto.AgentPerformanceMetricDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .ticketsHandled(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .averageResponseTime(null)
            .satisfactionScore(null)
            .escalations(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AnalyticsReportResponseDto.AgentPerformanceMetricDto dto = AnalyticsReportResponseDto.AgentPerformanceMetricDto.builder()
                        .agentId("test-agentId")
            .agentName("test-agentName")
            .ticketsHandled(42)
            .ticketsResolved(42)
            .averageResolutionTime(null)
            .averageResponseTime(null)
            .satisfactionScore(null)
            .escalations(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
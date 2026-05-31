package com.gogidix.finance.compliance.application.dto.response;

import com.gogidix.finance.compliance.application.dto.response.ComplianceReportResponseDto;
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
class ComplianceReportResponseDto_ComplianceMetricDtoTest {

        @Test
    void testBuilder() {
        ComplianceReportResponseDto.ComplianceMetricDto dto = ComplianceReportResponseDto.ComplianceMetricDto.builder()
                        .metricName("test-metricName")
            .metricType("test-metricType")
            .value(null)
            .targetValue("test-targetValue")
            .status("test-status")
            .trend("test-trend")
            .build();
        assertNotNull(dto);
        assertEquals("test-metricName", dto.getMetricName());
        assertEquals("test-metricType", dto.getMetricType());
        assertEquals("test-targetValue", dto.getTargetValue());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-trend", dto.getTrend());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceReportResponseDto.ComplianceMetricDto dto = new ComplianceReportResponseDto.ComplianceMetricDto();
        dto.setMetricName("val-metricName");
        dto.setMetricType("val-metricType");
        dto.setTargetValue("val-targetValue");
        dto.setStatus("val-status");
        dto.setTrend("val-trend");
        assertEquals("val-metricName", dto.getMetricName());
        assertEquals("val-metricType", dto.getMetricType());
        assertEquals("val-targetValue", dto.getTargetValue());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-trend", dto.getTrend());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceReportResponseDto.ComplianceMetricDto dto1 = ComplianceReportResponseDto.ComplianceMetricDto.builder()
                        .metricName("test-metricName")
            .metricType("test-metricType")
            .value(null)
            .targetValue("test-targetValue")
            .status("test-status")
            .trend("test-trend")
            .build();
        ComplianceReportResponseDto.ComplianceMetricDto dto2 = ComplianceReportResponseDto.ComplianceMetricDto.builder()
                        .metricName("test-metricName")
            .metricType("test-metricType")
            .value(null)
            .targetValue("test-targetValue")
            .status("test-status")
            .trend("test-trend")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceReportResponseDto.ComplianceMetricDto dto = ComplianceReportResponseDto.ComplianceMetricDto.builder()
                        .metricName("test-metricName")
            .metricType("test-metricType")
            .value(null)
            .targetValue("test-targetValue")
            .status("test-status")
            .trend("test-trend")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
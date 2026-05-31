package com.gogidix.finance.compliance.domain.model;

import com.gogidix.finance.compliance.domain.model.ComplianceReport;
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
class ComplianceReport_ComplianceMetricTest {

        @Test
    void testBuilder() {
        ComplianceReport.ComplianceMetric dto = ComplianceReport.ComplianceMetric.builder()
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
        ComplianceReport.ComplianceMetric dto = new ComplianceReport.ComplianceMetric();
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
        ComplianceReport.ComplianceMetric dto1 = ComplianceReport.ComplianceMetric.builder()
                        .metricName("test-metricName")
            .metricType("test-metricType")
            .value(null)
            .targetValue("test-targetValue")
            .status("test-status")
            .trend("test-trend")
            .build();
        ComplianceReport.ComplianceMetric dto2 = ComplianceReport.ComplianceMetric.builder()
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
        ComplianceReport.ComplianceMetric dto = ComplianceReport.ComplianceMetric.builder()
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
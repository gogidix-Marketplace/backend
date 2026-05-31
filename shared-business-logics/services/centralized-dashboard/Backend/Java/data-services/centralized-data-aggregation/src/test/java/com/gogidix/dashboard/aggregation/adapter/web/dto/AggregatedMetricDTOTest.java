package com.gogidix.dashboard.aggregation.adapter.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregatedMetricDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        assertNotNull(new AggregatedMetricDTO());
    }

    @Test
    void threeArgConstructor_setsFields() {
        AggregatedMetricDTO dto = new AggregatedMetricDTO("id1", "Revenue", 100.0);
        assertEquals("id1", dto.getId());
        assertEquals("Revenue", dto.getMetricName());
        assertEquals(100.0, dto.getValue());
        assertNotNull(dto.getTimestamp());
    }

    @Test
    void builder_createsFullDTO() {
        LocalDateTime now = LocalDateTime.now();
        AggregatedMetricDTO dto = new AggregatedMetricDTO.Builder()
                .withId("id1")
                .withMetricName("Revenue")
                .withValue(100.0)
                .withUnit("USD")
                .withAggregationFunction("SUM")
                .withTimestamp(now)
                .withTimeWindow(now.minusHours(1), now)
                .withGranularity("HOURLY")
                .withDimensions(Map.of("region", "NA"))
                .withMetadata(Map.of("source", "db"))
                .withConfidenceScore(95.0)
                .withStatistics(1000L, 10.0, 200.0, 50.0, 15.0)
                .withQualityIndicators("GOOD", false, false)
                .build();

        assertEquals("id1", dto.getId());
        assertEquals("Revenue", dto.getMetricName());
        assertEquals(100.0, dto.getValue());
        assertEquals("USD", dto.getUnit());
        assertEquals("SUM", dto.getAggregationFunction());
        assertEquals(now, dto.getTimestamp());
        assertEquals(now.minusHours(1), dto.getWindowStart());
        assertEquals(now, dto.getWindowEnd());
        assertEquals("HOURLY", dto.getGranularity());
        assertEquals(Map.of("region", "NA"), dto.getDimensions());
        assertEquals(Map.of("source", "db"), dto.getMetadata());
        assertEquals(95.0, dto.getConfidenceScore());
        assertEquals(1000L, dto.getSampleSize());
        assertEquals(10.0, dto.getMinValue());
        assertEquals(200.0, dto.getMaxValue());
        assertEquals(50.0, dto.getAvgValue());
        assertEquals(15.0, dto.getStdDeviation());
        assertEquals("GOOD", dto.getDataQualityStatus());
        assertFalse(dto.getIsStale());
        assertFalse(dto.getHasAnomalies());
    }

    @Test
    void setters_workCorrectly() {
        AggregatedMetricDTO dto = new AggregatedMetricDTO();
        dto.setId("id");
        dto.setMetricName("name");
        dto.setValue(42.0);
        dto.setUnit("kg");
        dto.setAggregationFunction("AVG");
        dto.setTimestamp(LocalDateTime.now());
        dto.setWindowStart(LocalDateTime.now());
        dto.setWindowEnd(LocalDateTime.now());
        dto.setGranularity("DAILY");
        dto.setDimensions(Map.of());
        dto.setMetadata(Map.of());
        dto.setConfidenceScore(80.0);
        dto.setSampleSize(100L);
        dto.setMinValue(1.0);
        dto.setMaxValue(10.0);
        dto.setAvgValue(5.0);
        dto.setStdDeviation(2.0);
        dto.setSourceDomain("COURIER");
        dto.setDataQualityStatus("FAIR");
        dto.setIsStale(true);
        dto.setHasAnomalies(true);

        assertEquals("id", dto.getId());
        assertEquals("name", dto.getMetricName());
        assertEquals(42.0, dto.getValue());
        assertEquals("kg", dto.getUnit());
        assertTrue(dto.getIsStale());
        assertTrue(dto.getHasAnomalies());
    }
}

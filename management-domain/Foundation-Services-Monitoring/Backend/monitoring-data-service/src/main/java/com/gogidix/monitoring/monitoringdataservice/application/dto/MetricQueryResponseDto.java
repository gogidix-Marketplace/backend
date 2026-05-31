package com.gogidix.monitoring.monitoringdataservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for metric query results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricQueryResponseDto {

    private String serviceName;
    private String metricName;
    private Instant startTime;
    private Instant endTime;
    private Long dataPointCount;
    private MetricStatistics statistics;
    private java.util.List<DataPoint> dataPoints;
    private Map<String, String> tags;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricStatistics {
        private Double min;
        private Double max;
        private Double avg;
        private Double sum;
        private Double p50;
        private Double p95;
        private Double p99;
        private Double stdDev;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataPoint {
        private Instant timestamp;
        private Double value;
    }
}

package com.gogidix.management.executive.workflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Performance Benchmark domain model
 * Represents performance benchmarks for KPI metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "performance_benchmarks")
public class PerformanceBenchmark extends BaseEntity {

    private String name;
    private String description;
    private String metricType;
    private BigDecimal targetValue;
    private BigDecimal thresholdMin;
    private BigDecimal thresholdMax;
    private BenchmarkType type;
    private String timeFrame;
    private Map<String, Object> metadata;

    public enum BenchmarkType {
        INTERNAL, INDUSTRY, CUSTOM, REGULATORY
    }

    /**
     * Check if a value meets the benchmark target
     */
    public boolean meetsTarget(BigDecimal actualValue) {
        if (actualValue == null || targetValue == null) {
            return false;
        }

        switch (type) {
            case INTERNAL:
            case CUSTOM:
                return actualValue.compareTo(targetValue) >= 0;
            case INDUSTRY:
            case REGULATORY:
                if (thresholdMin != null && actualValue.compareTo(thresholdMin) < 0) {
                    return false;
                }
                if (thresholdMax != null && actualValue.compareTo(thresholdMax) > 0) {
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    /**
     * Calculate performance percentage against target
     */
    public double calculatePerformance(BigDecimal actualValue) {
        if (actualValue == null || targetValue == null || targetValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return actualValue.divide(targetValue, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}

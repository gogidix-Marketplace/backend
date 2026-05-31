package com.gogidix.aiservices.timeseriesforecasting.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Domain service for detecting anomalies in time series data.
 */
@Service
public class AnomalyDetectionService {

    private static final Logger log = LoggerFactory.getLogger(AnomalyDetectionService.class);
    private static final double DEFAULT_CONFIDENCE = 0.95;
    private static final int Z_SCORE_THRESHOLD = 3;

    /**
     * Detects anomalies in time series data.
     */
    public List<Anomaly> detectAnomalies(List<TimeSeriesDataPoint> timeSeriesData, double confidence) {
        log.info("Detecting anomalies in {} data points", timeSeriesData.size());

        if (timeSeriesData == null || timeSeriesData.size() < 20) {
            throw new IllegalArgumentException(
                "At least 20 data points are required for anomaly detection");
        }

        List<Anomaly> anomalies = new ArrayList<>();

        // Calculate mean and standard deviation
        double mean = timeSeriesData.stream()
            .mapToDouble(TimeSeriesDataPoint::getValue)
            .average()
            .orElse(0);

        double stdDev = Math.sqrt(timeSeriesData.stream()
            .mapToDouble(dp -> Math.pow(dp.getValue() - mean, 2))
            .average()
            .orElse(0));

        // Detect outliers using Z-score
        for (TimeSeriesDataPoint dataPoint : timeSeriesData) {
            double zScore = stdDev > 0 ? (dataPoint.getValue() - mean) / stdDev : 0;

            if (Math.abs(zScore) > Z_SCORE_THRESHOLD) {
                anomalies.add(new Anomaly(
                    dataPoint.getTimestamp(),
                    dataPoint.getValue(),
                    zScore,
                    calculateDeviationPercentage(dataPoint.getValue(), mean),
                    AnomalySeverity.valueOf(Math.abs(zScore))
                ));
            }
        }

        log.info("Detected {} anomalies", anomalies.size());
        return anomalies;
    }

    /**
     * Detects anomalies with default confidence level.
     */
    public List<Anomaly> detectAnomalies(List<TimeSeriesDataPoint> timeSeriesData) {
        return detectAnomalies(timeSeriesData, DEFAULT_CONFIDENCE);
    }

    private double calculateDeviationPercentage(double actual, double expected) {
        if (expected == 0) {
            return actual > 0 ? 100.0 : 0.0;
        }
        return Math.abs((actual - expected) / expected) * 100;
    }

    public static class Anomaly {
        private final LocalDateTime timestamp;
        private final Double value;
        private final Double zScore;
        private final Double deviationPercentage;
        private final AnomalySeverity severity;

        public Anomaly(LocalDateTime timestamp, Double value, Double zScore,
                      Double deviationPercentage, AnomalySeverity severity) {
            this.timestamp = timestamp;
            this.value = value;
            this.zScore = zScore;
            this.deviationPercentage = deviationPercentage;
            this.severity = severity;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public Double getValue() {
            return value;
        }

        public Double getZScore() {
            return zScore;
        }

        public Double getDeviationPercentage() {
            return deviationPercentage;
        }

        public AnomalySeverity getSeverity() {
            return severity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Anomaly anomaly = (Anomaly) o;
            return Objects.equals(timestamp, anomaly.timestamp) &&
                   Objects.equals(value, anomaly.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(timestamp, value);
        }
    }

    public enum AnomalySeverity {
        LOW(2.0, 3.0),
        MEDIUM(3.0, 4.0),
        HIGH(4.0, 5.0),
        CRITICAL(5.0, Double.MAX_VALUE);

        private final double minZScore;
        private final double maxZScore;

        AnomalySeverity(double minZScore, double maxZScore) {
            this.minZScore = minZScore;
            this.maxZScore = maxZScore;
        }

        public static AnomalySeverity valueOf(double zScore) {
            double absZScore = Math.abs(zScore);
            for (AnomalySeverity severity : values()) {
                if (absZScore >= severity.minZScore && absZScore < severity.maxZScore) {
                    return severity;
                }
            }
            return CRITICAL;
        }
    }
}

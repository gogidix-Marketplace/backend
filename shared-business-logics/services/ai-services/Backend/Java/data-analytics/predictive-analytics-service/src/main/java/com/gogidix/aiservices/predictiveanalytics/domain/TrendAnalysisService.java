package com.gogidix.aiservices.predictiveanalytics.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain service for trend analysis.
 */
@Service
public class TrendAnalysisService {

    private static final int MIN_HISTORICAL_DATA_POINTS = 50;
    private static final double DEFAULT_CONFIDENCE_LEVEL = 0.95;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TrendAnalysisService.class);

    /**
     * Analyzes trends in the given data.
     */
    public TrendResult analyzeTrends(Map<String, Object> data, String targetField) {
        log.info("Analyzing trends for field: {}", targetField);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> records = (List<Map<String, Object>>) data.get("data");

        if (records == null || records.size() < MIN_HISTORICAL_DATA_POINTS) {
            throw new InsufficientDataException(
                "Insufficient historical data. Minimum " + MIN_HISTORICAL_DATA_POINTS + " periods required");
        }

        // Extract values
        List<Double> values = records.stream()
            .map(r -> (Number) r.get(targetField))
            .map(Number::doubleValue)
            .toList();

        // Calculate trend
        TrendDirection direction = calculateTrendDirection(values);
        double strength = calculateTrendStrength(values);
        double seasonality = detectSeasonality(values);

        return new TrendResult(
            targetField,
            direction,
            strength,
            seasonality,
            calculateSlope(values),
            DEFAULT_CONFIDENCE_LEVEL
        );
    }

    private TrendDirection calculateTrendDirection(List<Double> values) {
        if (values.size() < 2) {
            return TrendDirection.STABLE;
        }

        double firstHalfAvg = values.subList(0, values.size() / 2).stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);
        double secondHalfAvg = values.subList(values.size() / 2, values.size()).stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);

        if (secondHalfAvg > firstHalfAvg * 1.05) {
            return TrendDirection.INCREASING;
        } else if (secondHalfAvg < firstHalfAvg * 0.95) {
            return TrendDirection.DECREASING;
        }
        return TrendDirection.STABLE;
    }

    private double calculateTrendStrength(List<Double> values) {
        // Calculate coefficient of determination (R^2)
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double ssTot = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).sum();
        double ssRes = 0;

        for (int i = 0; i < values.size(); i++) {
            double predicted = mean + (i - values.size() / 2.0) * calculateSlope(values);
            ssRes += Math.pow(values.get(i) - predicted, 2);
        }

        return ssTot > 0 ? 1 - (ssRes / ssTot) : 0;
    }

    private double calculateSlope(List<Double> values) {
        if (values.size() < 2) {
            return 0;
        }

        double n = values.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += i;
            sumY += values.get(i);
            sumXY += i * values.get(i);
            sumX2 += i * i;
        }

        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }

    private double detectSeasonality(List<Double> values) {
        // Simple autocorrelation for seasonality detection
        if (values.size() < 12) {
            return 0.0;
        }

        int period = Math.min(12, values.size() / 4);
        double correlation = 0;

        for (int lag = 1; lag <= period; lag++) {
            double sum = 0;
            int count = 0;
            for (int i = lag; i < values.size(); i++) {
                sum += values.get(i) * values.get(i - lag);
                count++;
            }
            correlation = Math.max(correlation, sum / count);
        }

        return Math.min(1.0, Math.max(0.0, correlation));
    }

    public static class TrendResult {
        private final String field;
        private final TrendDirection direction;
        private final double strength;
        private final double seasonality;
        private final double slope;
        private final double confidenceLevel;

        public TrendResult(String field, TrendDirection direction, double strength,
                          double seasonality, double slope, double confidenceLevel) {
            this.field = field;
            this.direction = direction;
            this.strength = strength;
            this.seasonality = seasonality;
            this.slope = slope;
            this.confidenceLevel = confidenceLevel;
        }

        public String getField() {
            return field;
        }

        public TrendDirection getDirection() {
            return direction;
        }

        public double getStrength() {
            return strength;
        }

        public double getSeasonality() {
            return seasonality;
        }

        public double getSlope() {
            return slope;
        }

        public double getConfidenceLevel() {
            return confidenceLevel;
        }
    }

    public enum TrendDirection {
        INCREASING,
        DECREASING,
        STABLE
    }
}

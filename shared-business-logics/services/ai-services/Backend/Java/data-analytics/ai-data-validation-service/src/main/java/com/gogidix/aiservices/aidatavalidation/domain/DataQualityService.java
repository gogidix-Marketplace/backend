package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Domain service for analyzing data quality.
 */
public class DataQualityService {

    /**
     * Analyzes a batch of data records for quality issues.
     */
    public List<DataQualityIssue> analyzeQualityBatch(List<Map<String, Object>> dataRecords) {
        List<DataQualityIssue> issues = new ArrayList<>();

        if (dataRecords == null || dataRecords.isEmpty()) {
            return issues;
        }

        for (Map<String, Object> dataRecord : dataRecords) {
            issues.addAll(analyzeQuality(dataRecord));
        }

        return issues;
    }

    /**
     * Analyzes a data record for quality issues.
     */
    public List<DataQualityIssue> analyzeQuality(Map<String, Object> dataRecord) {
        List<DataQualityIssue> issues = new ArrayList<>();

        if (dataRecord == null || dataRecord.isEmpty()) {
            issues.add(new DataQualityIssue("record", QualitySeverity.CRITICAL, "Empty or null record"));
            return issues;
        }

        // Check for empty strings
        dataRecord.forEach((key, value) -> {
            if (value instanceof String && ((String) value).trim().isEmpty()) {
                issues.add(new DataQualityIssue(key, QualitySeverity.HIGH, "Empty string value"));
            }
            // Check for negative numbers where inappropriate
            if (value instanceof Number && ((Number) value).doubleValue() < 0) {
                issues.add(new DataQualityIssue(key, QualitySeverity.MEDIUM, "Negative number detected"));
            }
        });

        return issues;
    }

    /**
     * Detects anomalies in a batch of data records.
     */
    public List<Anomaly> detectAnomaliesInBatch(List<Map<String, Object>> records) {
        List<Anomaly> anomalies = new ArrayList<>();

        if (records == null || records.isEmpty()) {
            return anomalies;
        }

        // Get all field names
        Map<String, List<Double>> fieldValues = new java.util.HashMap<>();
        for (Map<String, Object> record : records) {
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                if (entry.getValue() instanceof Number) {
                    fieldValues.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                        .add(((Number) entry.getValue()).doubleValue());
                }
            }
        }

        // Detect anomalies for each field
        for (Map.Entry<String, List<Double>> entry : fieldValues.entrySet()) {
            List<Double> values = entry.getValue();
            if (values.size() >= 3) {
                double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                double stdDev = Math.sqrt(values.stream()
                    .mapToDouble(v -> Math.pow(v - mean, 2))
                    .average().orElse(0.0));

                double threshold = 2.0 * stdDev;

                for (Double value : values) {
                    double deviation = Math.abs(value - mean);
                    if (deviation > threshold) {
                        anomalies.add(new Anomaly(entry.getKey(), value, deviation));
                    }
                }
            }
        }

        return anomalies;
    }

    /**
     * Calculates completeness score for a dataset.
     */
    public double calculateCompleteness(List<Map<String, Object>> records) {
        if (records == null || records.isEmpty()) {
            return 0.0;
        }

        int totalFields = 0;
        int nullFields = 0;

        for (Map<String, Object> record : records) {
            if (record != null) {
                int recordSize = record.size();
                totalFields += recordSize;
                nullFields += record.values().stream()
                    .mapToInt(v -> v == null ? 1 : 0)
                    .sum();
            }
        }

        return totalFields == 0 ? 0.0 : 1.0 - ((double) nullFields / totalFields);
    }

    /**
     * Detects anomalies in numeric fields using statistical methods.
     */
    public List<Anomaly> detectAnomalies(List<Map<String, Object>> records, String fieldName) {
        List<Anomaly> anomalies = new ArrayList<>();

        if (records == null || records.isEmpty()) {
            return anomalies;
        }

        List<Double> values = records.stream()
            .filter(r -> r.containsKey(fieldName))
            .map(r -> {
                Object val = r.get(fieldName);
                if (val instanceof Number) {
                    return ((Number) val).doubleValue();
                }
                return null;
            })
            .filter(v -> v != null)
            .collect(Collectors.toList());

        if (values.size() < 3) {
            return anomalies;
        }

        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double stdDev = Math.sqrt(values.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average().orElse(0.0));

        double threshold = 2.0 * stdDev;

        for (int i = 0; i < records.size(); i++) {
            Map<String, Object> record = records.get(i);
            if (record.containsKey(fieldName)) {
                Object val = record.get(fieldName);
                if (val instanceof Number) {
                    double numValue = ((Number) val).doubleValue();
                    if (Math.abs(numValue - mean) > threshold) {
                        anomalies.add(new Anomaly(fieldName, numValue, Math.abs(numValue - mean)));
                    }
                }
            }
        }

        return anomalies;
    }
}

/**
 * Value object for data quality issues.
 */
@Value
class DataQualityIssueInner {
    String fieldName;
    QualitySeverity severity;
    String description;
}

/**
 * Quality severity levels.
 */
enum QualitySeverityInner {
    LOW, MEDIUM, HIGH, CRITICAL
}

/**
 * Represents an anomaly detected in data.
 */
@Value
class AnomalyInner {
    String fieldName;
    Double value;
    int recordIndex;
    Double deviation;
}

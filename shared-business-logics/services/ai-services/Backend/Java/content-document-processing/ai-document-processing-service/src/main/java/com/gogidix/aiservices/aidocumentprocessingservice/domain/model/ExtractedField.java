package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Builder
public class ExtractedField {
    private final String name;
    private final String value;
    private final double confidence;
    @Builder.Default
    private final Map<String, Object> metadata = Map.of();

    public ExtractedField(String name, String value, double confidence, Map<String, Object> metadata) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("Confidence must be between 0 and 1");
        }
        this.name = name;
        this.value = value;
        this.confidence = BigDecimal.valueOf(confidence).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.metadata = metadata != null ? metadata : Map.of();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public double getConfidence() {
        return confidence;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public boolean meetsThreshold(double threshold) {
        return confidence >= threshold;
    }

    public boolean isValidRequired() {
        return value != null && !value.trim().isEmpty();
    }

    public boolean isValid(double threshold) {
        return isValidRequired() && meetsThreshold(threshold);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBoundingBox() {
        Object position = metadata.get("position");
        if (position instanceof Map) {
            return (Map<String, Object>) position;
        }
        return null;
    }

    public int getPageNumber() {
        Object page = metadata.get("page");
        if (page instanceof Number) {
            return ((Number) page).intValue();
        }
        return 1;
    }

    public boolean isNumeric() {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDate() {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Double getAsNumeric() {
        if (isNumeric()) {
            return new BigDecimal(value).doubleValue();
        }
        return null;
    }

    public LocalDate getAsDate() {
        if (isDate()) {
            return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
        }
        return null;
    }
}

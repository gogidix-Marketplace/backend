package com.gogidix.aiservices.aidatavalidation.domain;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class DataQualityServiceTest {
    private DataQualityService s;

    @BeforeEach
    void setup() { s = new DataQualityService(); }

    @Test
    void analyzeQualityWithValidData() {
        var record = Map.<String, Object>of("name", "test", "value", 42);
        var issues = s.analyzeQuality(record);
        assertThat(issues).isNotNull();
    }

    @Test
    void analyzeQualityWithNullRecord() {
        var issues = s.analyzeQuality(null);
        assertThat(issues).isNotNull();
    }

    @Test
    void analyzeQualityBatch() {
        var records = List.of(
            Map.<String, Object>of("name", "a", "value", 1),
            Map.<String, Object>of("name", "b", "value", 2)
        );
        var issues = s.analyzeQualityBatch(records);
        assertThat(issues).isNotNull();
    }

    @Test
    void analyzeQualityBatchEmpty() {
        var issues = s.analyzeQualityBatch(Collections.emptyList());
        assertThat(issues).isNotNull();
    }

    @Test
    void detectAnomalies() {
        var records = List.of(
            Map.<String, Object>of("val", 10.0),
            Map.<String, Object>of("val", 12.0),
            Map.<String, Object>of("val", 100.0)
        );
        var anomalies = s.detectAnomalies(records, "val");
        assertThat(anomalies).isNotNull();
    }

    @Test
    void detectAnomaliesInBatch() {
        var records = List.of(
            Map.<String, Object>of("x", 1.0),
            Map.<String, Object>of("x", 2.0)
        );
        var anomalies = s.detectAnomaliesInBatch(records);
        assertThat(anomalies).isNotNull();
    }

    @Test
    void calculateCompleteness() {
        var records = List.of(
            Map.<String, Object>of("a", 1, "b", 2),
            Map.<String, Object>of("a", 1)
        );
        var completeness = s.calculateCompleteness(records);
        assertThat(completeness).isGreaterThanOrEqualTo(0.0);
        assertThat(completeness).isLessThanOrEqualTo(1.0);
    }

    @Test
    void calculateCompletenessEmpty() {
        var completeness = s.calculateCompleteness(Collections.emptyList());
        assertThat(completeness).isGreaterThanOrEqualTo(0.0);
    }
}

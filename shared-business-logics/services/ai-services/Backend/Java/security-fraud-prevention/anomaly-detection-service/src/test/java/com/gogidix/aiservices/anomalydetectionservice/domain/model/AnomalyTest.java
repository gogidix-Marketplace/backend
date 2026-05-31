package com.gogidix.aiservices.anomalydetectionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Anomaly Domain Model Tests")
class AnomalyTest {

    private static final String TIMESTAMP = "2025-03-07T12:00:00Z";
    private static final double SCORE = 0.95;
    private static final String DESCRIPTION = "Unusual spike in API response time";
    private static final String AFFECTED_METRIC = "response_time";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            Map<String, Object> metadata = Map.of("threshold", "100ms", "actual", "500ms");

            Anomaly anomaly = Anomaly.builder()
                    .timestamp(TIMESTAMP)
                    .score(SCORE)
                    .description(DESCRIPTION)
                    .affectedMetric(AFFECTED_METRIC)
                    .metadata(metadata)
                    .build();

            assertThat(anomaly.getTimestamp()).isEqualTo(TIMESTAMP);
            assertThat(anomaly.getScore()).isEqualTo(SCORE);
            assertThat(anomaly.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(anomaly.getAffectedMetric()).isEqualTo(AFFECTED_METRIC);
            assertThat(anomaly.getMetadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            Anomaly anomaly = Anomaly.builder()
                    .score(SCORE)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(SCORE);
        }

        @Test
        @DisplayName("Should build with null optional fields")
        void shouldBuildWithNullOptionalFields() {
            Anomaly anomaly = Anomaly.builder()
                    .timestamp(null)
                    .score(SCORE)
                    .description(null)
                    .affectedMetric(null)
                    .metadata(null)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(SCORE);
            assertThat(anomaly.getTimestamp()).isNull();
            assertThat(anomaly.getDescription()).isNull();
            assertThat(anomaly.getAffectedMetric()).isNull();
            assertThat(anomaly.getMetadata()).isNull();
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            Anomaly anomaly = Anomaly.builder()
                    .score(SCORE)
                    .description("Test")
                    .build();

            assertThat(anomaly.getDescription()).isEqualTo("Test");
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should set and get timestamp")
        void shouldSetAndGetTimestamp() {
            Anomaly anomaly = Anomaly.builder()
                    .timestamp(TIMESTAMP)
                    .build();

            assertThat(anomaly.getTimestamp()).isEqualTo(TIMESTAMP);
        }

        @Test
        @DisplayName("Should accept null timestamp")
        void shouldAcceptNullTimestamp() {
            Anomaly anomaly = Anomaly.builder()
                    .timestamp(null)
                    .build();

            assertThat(anomaly.getTimestamp()).isNull();
        }

        @Test
        @DisplayName("Should accept empty timestamp")
        void shouldAcceptEmptyTimestamp() {
            Anomaly anomaly = Anomaly.builder()
                    .timestamp("")
                    .build();

            assertThat(anomaly.getTimestamp()).isEmpty();
        }

        @Test
        @DisplayName("Should accept ISO-8601 format")
        void shouldAcceptIso8601Format() {
            String isoTimestamp = "2025-03-07T12:00:00.000Z";
            Anomaly anomaly = Anomaly.builder()
                    .timestamp(isoTimestamp)
                    .build();

            assertThat(anomaly.getTimestamp()).isEqualTo(isoTimestamp);
        }
    }

    @Nested
    @DisplayName("Score Tests")
    class ScoreTests {

        @Test
        @DisplayName("Should set and get score")
        void shouldSetAndGetScore() {
            Anomaly anomaly = Anomaly.builder()
                    .score(SCORE)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(SCORE);
        }

        @Test
        @DisplayName("Should accept score 0.0")
        void shouldAcceptScore0() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.0)
                    .build();

            assertThat(anomaly.getScore()).isZero();
        }

        @Test
        @DisplayName("Should accept score 1.0")
        void shouldAcceptScore1() {
            Anomaly anomaly = Anomaly.builder()
                    .score(1.0)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should accept negative score")
        void shouldAcceptNegativeScore() {
            Anomaly anomaly = Anomaly.builder()
                    .score(-0.5)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(-0.5);
        }

        @Test
        @DisplayName("Should accept score greater than 1")
        void shouldAcceptScoreGreaterThan1() {
            Anomaly anomaly = Anomaly.builder()
                    .score(1.5)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(1.5);
        }

        @Test
        @DisplayName("Should handle very small score")
        void shouldHandleVerySmallScore() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.001)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(0.001);
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            Anomaly anomaly = Anomaly.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(anomaly.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should accept null description")
        void shouldAcceptNullDescription() {
            Anomaly anomaly = Anomaly.builder()
                    .description(null)
                    .build();

            assertThat(anomaly.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should accept empty description")
        void shouldAcceptEmptyDescription() {
            Anomaly anomaly = Anomaly.builder()
                    .description("")
                    .build();

            assertThat(anomaly.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should accept long description")
        void shouldAcceptLongDescription() {
            String longDescription = "This is a detailed description of the anomaly ".repeat(10);
            Anomaly anomaly = Anomaly.builder()
                    .description(longDescription)
                    .build();

            assertThat(anomaly.getDescription()).isEqualTo(longDescription);
        }
    }

    @Nested
    @DisplayName("AffectedMetric Tests")
    class AffectedMetricTests {

        @Test
        @DisplayName("Should set and get affected metric")
        void shouldSetAndGetAffectedMetric() {
            Anomaly anomaly = Anomaly.builder()
                    .affectedMetric(AFFECTED_METRIC)
                    .build();

            assertThat(anomaly.getAffectedMetric()).isEqualTo(AFFECTED_METRIC);
        }

        @Test
        @DisplayName("Should accept null affected metric")
        void shouldAcceptNullAffectedMetric() {
            Anomaly anomaly = Anomaly.builder()
                    .affectedMetric(null)
                    .build();

            assertThat(anomaly.getAffectedMetric()).isNull();
        }

        @Test
        @DisplayName("Should accept various metric names")
        void shouldAcceptVariousMetricNames() {
            String[] metrics = {"cpu_usage", "memory_usage", "response_time", "error_rate", "throughput"};

            for (String metric : metrics) {
                Anomaly anomaly = Anomaly.builder()
                        .affectedMetric(metric)
                        .build();

                assertThat(anomaly.getAffectedMetric()).isEqualTo(metric);
            }
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should set and get metadata")
        void shouldSetAndGetMetadata() {
            Map<String, Object> metadata = Map.of("key1", "value1", "key2", 123);

            Anomaly anomaly = Anomaly.builder()
                    .metadata(metadata)
                    .build();

            assertThat(anomaly.getMetadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should accept null metadata")
        void shouldAcceptNullMetadata() {
            Anomaly anomaly = Anomaly.builder()
                    .metadata(null)
                    .build();

            assertThat(anomaly.getMetadata()).isNull();
        }

        @Test
        @DisplayName("Should accept empty metadata")
        void shouldAcceptEmptyMetadata() {
            Anomaly anomaly = Anomaly.builder()
                    .metadata(Map.of())
                    .build();

            assertThat(anomaly.getMetadata()).isEmpty();
        }

        @Test
        @DisplayName("Should accept metadata with various value types")
        void shouldAcceptMetadataWithVariousValueTypes() {
            Map<String, Object> metadata = Map.of(
                    "string", "value",
                    "integer", 42,
                    "double", 3.14,
                    "boolean", true
            );

            Anomaly anomaly = Anomaly.builder()
                    .metadata(metadata)
                    .build();

            assertThat(anomaly.getMetadata()).hasSize(4);
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            Anomaly anomaly1 = Anomaly.builder()
                    .score(SCORE)
                    .description(DESCRIPTION)
                    .build();

            Anomaly anomaly2 = Anomaly.builder()
                    .score(SCORE)
                    .description(DESCRIPTION)
                    .build();

            assertThat(anomaly1).isEqualTo(anomaly2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            Anomaly anomaly1 = Anomaly.builder()
                    .score(SCORE)
                    .build();

            Anomaly anomaly2 = Anomaly.builder()
                    .score(SCORE)
                    .build();

            assertThat(anomaly1.hashCode()).isEqualTo(anomaly2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            Anomaly anomaly = Anomaly.builder()
                    .score(SCORE)
                    .description(DESCRIPTION)
                    .build();

            String toString = anomaly.toString();

            assertThat(toString).contains(String.valueOf(SCORE));
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.5)
                    .build();

            anomaly.setScore(0.9);
            anomaly.setDescription("Updated");

            assertThat(anomaly.getScore()).isEqualTo(0.9);
            assertThat(anomaly.getDescription()).isEqualTo("Updated");
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get timestamp")
        void shouldGetTimestamp() {
            Anomaly anomaly = Anomaly.builder()
                    .timestamp(TIMESTAMP)
                    .build();

            assertThat(anomaly.getTimestamp()).isEqualTo(TIMESTAMP);
        }

        @Test
        @DisplayName("Should get score")
        void shouldGetScore() {
            Anomaly anomaly = Anomaly.builder()
                    .score(SCORE)
                    .build();

            assertThat(anomaly.getScore()).isEqualTo(SCORE);
        }

        @Test
        @DisplayName("Should get description")
        void shouldGetDescription() {
            Anomaly anomaly = Anomaly.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(anomaly.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should get affected metric")
        void shouldGetAffectedMetric() {
            Anomaly anomaly = Anomaly.builder()
                    .affectedMetric(AFFECTED_METRIC)
                    .build();

            assertThat(anomaly.getAffectedMetric()).isEqualTo(AFFECTED_METRIC);
        }

        @Test
        @DisplayName("Should get metadata")
        void shouldGetMetadata() {
            Map<String, Object> metadata = Map.of("key", "value");
            Anomaly anomaly = Anomaly.builder()
                    .metadata(metadata)
                    .build();

            assertThat(anomaly.getMetadata()).isEqualTo(metadata);
        }
    }

    @Nested
    @DisplayName("Severity Tests")
    class SeverityTests {

        @Test
        @DisplayName("High score indicates high severity")
        void highScoreIndicatesHighSeverity() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.9)
                    .build();

            assertThat(anomaly.getScore()).isGreaterThan(0.8);
        }

        @Test
        @DisplayName("Medium score indicates medium severity")
        void mediumScoreIndicatesMediumSeverity() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.5)
                    .build();

            assertThat(anomaly.getScore()).isBetween(0.4, 0.6);
        }

        @Test
        @DisplayName("Low score indicates low severity")
        void lowScoreIndicatesLowSeverity() {
            Anomaly anomaly = Anomaly.builder()
                    .score(0.2)
                    .build();

            assertThat(anomaly.getScore()).isLessThan(0.3);
        }
    }
}

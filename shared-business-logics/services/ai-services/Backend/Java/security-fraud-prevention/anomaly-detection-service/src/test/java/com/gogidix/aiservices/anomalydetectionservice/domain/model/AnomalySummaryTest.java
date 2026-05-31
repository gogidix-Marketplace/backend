package com.gogidix.aiservices.anomalydetectionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnomalySummary Domain Model Tests")
class AnomalySummaryTest {

    private static final int TOTAL_ANOMALIES = 100;
    private static final int HIGH_SEVERITY = 10;
    private static final int MEDIUM_SEVERITY = 30;
    private static final int LOW_SEVERITY = 60;
    private static final double AVERAGE_SCORE = 0.65;

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .highSeverity(HIGH_SEVERITY)
                    .mediumSeverity(MEDIUM_SEVERITY)
                    .lowSeverity(LOW_SEVERITY)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            assertThat(summary.getTotalAnomalies()).isEqualTo(TOTAL_ANOMALIES);
            assertThat(summary.getHighSeverity()).isEqualTo(HIGH_SEVERITY);
            assertThat(summary.getMediumSeverity()).isEqualTo(MEDIUM_SEVERITY);
            assertThat(summary.getLowSeverity()).isEqualTo(LOW_SEVERITY);
            assertThat(summary.getAverageScore()).isEqualTo(AVERAGE_SCORE);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .build();

            assertThat(summary.getTotalAnomalies()).isEqualTo(TOTAL_ANOMALIES);
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(50)
                    .averageScore(0.75)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(0.75);
        }
    }

    @Nested
    @DisplayName("TotalAnomalies Tests")
    class TotalAnomaliesTests {

        @Test
        @DisplayName("Should set and get total anomalies")
        void shouldSetAndGetTotalAnomalies() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .build();

            assertThat(summary.getTotalAnomalies()).isEqualTo(TOTAL_ANOMALIES);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 100, 1000})
        @DisplayName("Should accept various total counts")
        void shouldAcceptVariousTotalCounts(int total) {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(total)
                    .build();

            assertThat(summary.getTotalAnomalies()).isEqualTo(total);
        }

        @Test
        @DisplayName("Should accept zero total")
        void shouldAcceptZeroTotal() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(0)
                    .build();

            assertThat(summary.getTotalAnomalies()).isZero();
        }
    }

    @Nested
    @DisplayName("HighSeverity Tests")
    class HighSeverityTests {

        @Test
        @DisplayName("Should set and get high severity")
        void shouldSetAndGetHighSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .highSeverity(HIGH_SEVERITY)
                    .build();

            assertThat(summary.getHighSeverity()).isEqualTo(HIGH_SEVERITY);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5, 10, 50})
        @DisplayName("Should accept various high severity counts")
        void shouldAcceptVariousHighSeverityCounts(int count) {
            AnomalySummary summary = AnomalySummary.builder()
                    .highSeverity(count)
                    .build();

            assertThat(summary.getHighSeverity()).isEqualTo(count);
        }

        @Test
        @DisplayName("Should accept zero high severity")
        void shouldAcceptZeroHighSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .highSeverity(0)
                    .build();

            assertThat(summary.getHighSeverity()).isZero();
        }
    }

    @Nested
    @DisplayName("MediumSeverity Tests")
    class MediumSeverityTests {

        @Test
        @DisplayName("Should set and get medium severity")
        void shouldSetAndGetMediumSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .mediumSeverity(MEDIUM_SEVERITY)
                    .build();

            assertThat(summary.getMediumSeverity()).isEqualTo(MEDIUM_SEVERITY);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 10, 30, 100})
        @DisplayName("Should accept various medium severity counts")
        void shouldAcceptVariousMediumSeverityCounts(int count) {
            AnomalySummary summary = AnomalySummary.builder()
                    .mediumSeverity(count)
                    .build();

            assertThat(summary.getMediumSeverity()).isEqualTo(count);
        }
    }

    @Nested
    @DisplayName("LowSeverity Tests")
    class LowSeverityTests {

        @Test
        @DisplayName("Should set and get low severity")
        void shouldSetAndGetLowSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .lowSeverity(LOW_SEVERITY)
                    .build();

            assertThat(summary.getLowSeverity()).isEqualTo(LOW_SEVERITY);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 20, 60, 200})
        @DisplayName("Should accept various low severity counts")
        void shouldAcceptVariousLowSeverityCounts(int count) {
            AnomalySummary summary = AnomalySummary.builder()
                    .lowSeverity(count)
                    .build();

            assertThat(summary.getLowSeverity()).isEqualTo(count);
        }
    }

    @Nested
    @DisplayName("AverageScore Tests")
    class AverageScoreTests {

        @Test
        @DisplayName("Should set and get average score")
        void shouldSetAndGetAverageScore() {
            AnomalySummary summary = AnomalySummary.builder()
                    .averageScore(AVERAGE_SCORE)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(AVERAGE_SCORE);
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.25, 0.5, 0.75, 1.0})
        @DisplayName("Should accept various average scores")
        void shouldAcceptVariousAverageScores(double score) {
            AnomalySummary summary = AnomalySummary.builder()
                    .averageScore(score)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should accept score greater than 1")
        void shouldAcceptScoreGreaterThan1() {
            AnomalySummary summary = AnomalySummary.builder()
                    .averageScore(1.5)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(1.5);
        }

        @Test
        @DisplayName("Should accept negative score")
        void shouldAcceptNegativeScore() {
            AnomalySummary summary = AnomalySummary.builder()
                    .averageScore(-0.5)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(-0.5);
        }
    }

    @Nested
    @DisplayName("Severity Distribution Tests")
    class SeverityDistributionTests {

        @Test
        @DisplayName("Total equals sum of severities")
        void totalEqualsSumOfSeverities() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .highSeverity(HIGH_SEVERITY)
                    .mediumSeverity(MEDIUM_SEVERITY)
                    .lowSeverity(LOW_SEVERITY)
                    .build();

            int sum = summary.getHighSeverity() + summary.getMediumSeverity() + summary.getLowSeverity();
            assertThat(summary.getTotalAnomalies()).isEqualTo(sum);
        }

        @Test
        @DisplayName("Should support only high severity")
        void shouldSupportOnlyHighSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(10)
                    .highSeverity(10)
                    .mediumSeverity(0)
                    .lowSeverity(0)
                    .build();

            assertThat(summary.getHighSeverity()).isEqualTo(10);
            assertThat(summary.getMediumSeverity()).isZero();
            assertThat(summary.getLowSeverity()).isZero();
        }

        @Test
        @DisplayName("Should support only medium severity")
        void shouldSupportOnlyMediumSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(20)
                    .highSeverity(0)
                    .mediumSeverity(20)
                    .lowSeverity(0)
                    .build();

            assertThat(summary.getHighSeverity()).isZero();
            assertThat(summary.getMediumSeverity()).isEqualTo(20);
            assertThat(summary.getLowSeverity()).isZero();
        }

        @Test
        @DisplayName("Should support only low severity")
        void shouldSupportOnlyLowSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(30)
                    .highSeverity(0)
                    .mediumSeverity(0)
                    .lowSeverity(30)
                    .build();

            assertThat(summary.getHighSeverity()).isZero();
            assertThat(summary.getMediumSeverity()).isZero();
            assertThat(summary.getLowSeverity()).isEqualTo(30);
        }
    }

    @Nested
    @DisplayName("Risk Assessment Tests")
    class RiskAssessmentTests {

        @Test
        @DisplayName("High count of high severity indicates critical")
        void highCountOfHighSeverityIndicatesCritical() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(100)
                    .highSeverity(50)
                    .mediumSeverity(30)
                    .lowSeverity(20)
                    .build();

            assertThat(summary.getHighSeverity()).isGreaterThan(summary.getMediumSeverity());
        }

        @Test
        @DisplayName("Balanced distribution indicates moderate risk")
        void balancedDistributionIndicatesModerateRisk() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(30)
                    .highSeverity(10)
                    .mediumSeverity(10)
                    .lowSeverity(10)
                    .build();

            assertThat(summary.getHighSeverity())
                    .isEqualTo(summary.getMediumSeverity())
                    .isEqualTo(summary.getLowSeverity());
        }

        @Test
        @DisplayName("Mostly low severity indicates low risk")
        void mostlyLowSeverityIndicatesLowRisk() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(100)
                    .highSeverity(5)
                    .mediumSeverity(10)
                    .lowSeverity(85)
                    .build();

            assertThat(summary.getLowSeverity()).isGreaterThan(summary.getHighSeverity() + summary.getMediumSeverity());
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            AnomalySummary summary1 = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            AnomalySummary summary2 = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            assertThat(summary1).isEqualTo(summary2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            AnomalySummary summary1 = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            AnomalySummary summary2 = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            assertThat(summary1.hashCode()).isEqualTo(summary2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .averageScore(AVERAGE_SCORE)
                    .build();

            String toString = summary.toString();

            assertThat(toString).contains(String.valueOf(TOTAL_ANOMALIES));
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(50)
                    .build();

            summary.setTotalAnomalies(100);
            summary.setAverageScore(0.85);

            assertThat(summary.getTotalAnomalies()).isEqualTo(100);
            assertThat(summary.getAverageScore()).isEqualTo(0.85);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get total anomalies")
        void shouldGetTotalAnomalies() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(TOTAL_ANOMALIES)
                    .build();

            assertThat(summary.getTotalAnomalies()).isEqualTo(TOTAL_ANOMALIES);
        }

        @Test
        @DisplayName("Should get high severity")
        void shouldGetHighSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .highSeverity(HIGH_SEVERITY)
                    .build();

            assertThat(summary.getHighSeverity()).isEqualTo(HIGH_SEVERITY);
        }

        @Test
        @DisplayName("Should get medium severity")
        void shouldGetMediumSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .mediumSeverity(MEDIUM_SEVERITY)
                    .build();

            assertThat(summary.getMediumSeverity()).isEqualTo(MEDIUM_SEVERITY);
        }

        @Test
        @DisplayName("Should get low severity")
        void shouldGetLowSeverity() {
            AnomalySummary summary = AnomalySummary.builder()
                    .lowSeverity(LOW_SEVERITY)
                    .build();

            assertThat(summary.getLowSeverity()).isEqualTo(LOW_SEVERITY);
        }

        @Test
        @DisplayName("Should get average score")
        void shouldGetAverageScore() {
            AnomalySummary summary = AnomalySummary.builder()
                    .averageScore(AVERAGE_SCORE)
                    .build();

            assertThat(summary.getAverageScore()).isEqualTo(AVERAGE_SCORE);
        }
    }

    @Nested
    @DisplayName("Empty Summary Tests")
    class EmptySummaryTests {

        @Test
        @DisplayName("Should represent empty summary")
        void shouldRepresentEmptySummary() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(0)
                    .highSeverity(0)
                    .mediumSeverity(0)
                    .lowSeverity(0)
                    .averageScore(0.0)
                    .build();

            assertThat(summary.getTotalAnomalies()).isZero();
            assertThat(summary.getHighSeverity()).isZero();
            assertThat(summary.getMediumSeverity()).isZero();
            assertThat(summary.getLowSeverity()).isZero();
            assertThat(summary.getAverageScore()).isZero();
        }

        @Test
        @DisplayName("Empty summary has no anomalies")
        void emptySummaryHasNoAnomalies() {
            AnomalySummary summary = AnomalySummary.builder()
                    .totalAnomalies(0)
                    .highSeverity(0)
                    .mediumSeverity(0)
                    .lowSeverity(0)
                    .build();

            assertThat(summary.getTotalAnomalies()).isZero();
        }
    }
}

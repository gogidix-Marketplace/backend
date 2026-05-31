package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("FraudPattern Domain Model Tests")
class FraudPatternTest {

    private static final String PATTERN_ID = "pattern-123";
    private static final String PATTERN_NAME = "Velocity Check";
    private static final String DESCRIPTION = "Multiple transactions in short time";
    private static final double CONFIDENCE_SCORE = 0.9;
    private static final Instant LAST_SEEN = Instant.now();
    private static final int OCCURRENCE_COUNT = 10;

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build fraud pattern with all fields")
        void shouldBuildFraudPatternWithAllFields() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern).isNotNull();
            assertThat(pattern.getPatternId()).isEqualTo(PATTERN_ID);
            assertThat(pattern.getPatternName()).isEqualTo(PATTERN_NAME);
            assertThat(pattern.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(pattern.getConfidenceScore()).isEqualTo(CONFIDENCE_SCORE);
            assertThat(pattern.getLastSeen()).isEqualTo(LAST_SEEN);
            assertThat(pattern.getOccurrenceCount()).isEqualTo(OCCURRENCE_COUNT);
        }

        @Test
        @DisplayName("Should build fraud pattern with required fields only")
        void shouldBuildFraudPatternWithRequiredFields() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern).isNotNull();
            assertThat(pattern.getPatternId()).isEqualTo(PATTERN_ID);
            assertThat(pattern.getPatternName()).isEqualTo(PATTERN_NAME);
        }

        @Test
        @DisplayName("Should create multiple independent instances")
        void shouldCreateMultipleIndependentInstances() {
            FraudPattern pattern1 = FraudPattern.builder()
                    .patternId("pattern-1")
                    .patternName("Pattern 1")
                    .description("Description 1")
                    .confidenceScore(0.8)
                    .lastSeen(Instant.now())
                    .occurrenceCount(5)
                    .build();

            FraudPattern pattern2 = FraudPattern.builder()
                    .patternId("pattern-2")
                    .patternName("Pattern 2")
                    .description("Description 2")
                    .confidenceScore(0.9)
                    .lastSeen(Instant.now())
                    .occurrenceCount(10)
                    .build();

            assertThat(pattern1.getPatternId()).isNotEqualTo(pattern2.getPatternId());
            assertThat(pattern1.getPatternName()).isNotEqualTo(pattern2.getPatternName());
            assertThat(pattern1.getConfidenceScore()).isNotEqualTo(pattern2.getConfidenceScore());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct patternId")
        void shouldReturnCorrectPatternId() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternId()).isEqualTo(PATTERN_ID);
        }

        @Test
        @DisplayName("Should return correct patternName")
        void shouldReturnCorrectPatternName() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternName()).isEqualTo(PATTERN_NAME);
        }

        @Test
        @DisplayName("Should return correct description")
        void shouldReturnCorrectDescription() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should return correct confidenceScore")
        void shouldReturnCorrectConfidenceScore() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getConfidenceScore()).isEqualTo(CONFIDENCE_SCORE);
        }

        @Test
        @DisplayName("Should return correct lastSeen")
        void shouldReturnCorrectLastSeen() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getLastSeen()).isEqualTo(LAST_SEEN);
        }

        @Test
        @DisplayName("Should return correct occurrenceCount")
        void shouldReturnCorrectOccurrenceCount() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getOccurrenceCount()).isEqualTo(OCCURRENCE_COUNT);
        }
    }

    @Nested
    @DisplayName("Confidence Score Tests")
    class ConfidenceScoreTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.1, 0.25, 0.5, 0.75, 0.9, 0.99, 1.0})
        @DisplayName("Should handle various confidence scores")
        void shouldHandleVariousConfidenceScores(double score) {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(score)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getConfidenceScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should handle minimum confidence score of 0.0")
        void shouldHandleMinimumConfidenceScore() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(0.0)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getConfidenceScore()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle maximum confidence score of 1.0")
        void shouldHandleMaximumConfidenceScore() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(1.0)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getConfidenceScore()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should handle very small confidence score")
        void shouldHandleVerySmallConfidenceScore() {
            double smallScore = 0.001;

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(smallScore)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getConfidenceScore()).isEqualTo(smallScore);
        }
    }

    @Nested
    @DisplayName("Occurrence Count Tests")
    class OccurrenceCountTests {

        @Test
        @DisplayName("Should handle zero occurrence count")
        void shouldHandleZeroOccurrenceCount() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(0)
                    .build();

            assertThat(pattern.getOccurrenceCount()).isZero();
        }

        @Test
        @DisplayName("Should handle single occurrence")
        void shouldHandleSingleOccurrence() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(1)
                    .build();

            assertThat(pattern.getOccurrenceCount()).isOne();
        }

        @Test
        @DisplayName("Should handle large occurrence count")
        void shouldHandleLargeOccurrenceCount() {
            int largeCount = 1000000;

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(largeCount)
                    .build();

            assertThat(pattern.getOccurrenceCount()).isEqualTo(largeCount);
        }

        @Test
        @DisplayName("Should handle negative occurrence count")
        void shouldHandleNegativeOccurrenceCount() {
            int negativeCount = -1;

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(negativeCount)
                    .build();

            assertThat(pattern.getOccurrenceCount()).isEqualTo(negativeCount);
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should handle past timestamp")
        void shouldHandlePastTimestamp() {
            Instant pastTimestamp = Instant.now().minusSeconds(3600);

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(pastTimestamp)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getLastSeen()).isEqualTo(pastTimestamp);
            assertThat(pattern.getLastSeen()).isBefore(Instant.now());
        }

        @Test
        @DisplayName("Should handle future timestamp")
        void shouldHandleFutureTimestamp() {
            Instant futureTimestamp = Instant.now().plusSeconds(3600);

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(futureTimestamp)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getLastSeen()).isEqualTo(futureTimestamp);
            assertThat(pattern.getLastSeen()).isAfter(Instant.now());
        }

        @Test
        @DisplayName("Should handle epoch start timestamp")
        void shouldHandleEpochStartTimestamp() {
            Instant epochStart = Instant.EPOCH;

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(epochStart)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getLastSeen()).isEqualTo(epochStart);
        }
    }

    @Nested
    @DisplayName("String Field Tests")
    class StringFieldTests {

        @Test
        @DisplayName("Should handle empty pattern name")
        void shouldHandleEmptyPatternName() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName("")
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternName()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty description")
        void shouldHandleEmptyDescription() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description("")
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty pattern ID")
        void shouldHandleEmptyPatternId() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("")
                    .patternName(PATTERN_NAME)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternId()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long pattern name")
        void shouldHandleVeryLongPatternName() {
            String longName = "a".repeat(1000);

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(longName)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternName()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle very long description")
        void shouldHandleVeryLongDescription() {
            String longDescription = "a".repeat(5000);

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(longDescription)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getDescription()).hasSize(5000);
        }

        @Test
        @DisplayName("Should handle special characters in pattern name")
        void shouldHandleSpecialCharactersInPatternName() {
            String specialName = "Pattern!@#$%^&*()_+-=[]{}|;':\",./<>?";

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(specialName)
                    .description(DESCRIPTION)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getPatternName()).isEqualTo(specialName);
        }

        @Test
        @DisplayName("Should handle unicode characters in description")
        void shouldHandleUnicodeCharactersInDescription() {
            String unicodeDescription = "Pattern with unicode: 中文 日本語 한글 العربية עברית";

            FraudPattern pattern = FraudPattern.builder()
                    .patternId(PATTERN_ID)
                    .patternName(PATTERN_NAME)
                    .description(unicodeDescription)
                    .confidenceScore(CONFIDENCE_SCORE)
                    .lastSeen(LAST_SEEN)
                    .occurrenceCount(OCCURRENCE_COUNT)
                    .build();

            assertThat(pattern.getDescription()).isEqualTo(unicodeDescription);
        }
    }

    @Nested
    @DisplayName("Pattern Type Tests")
    class PatternTypeTests {

        @Test
        @DisplayName("Should build velocity check pattern")
        void shouldBuildVelocityCheckPattern() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("velocity-001")
                    .patternName("Velocity Check")
                    .description("Multiple transactions in short time")
                    .confidenceScore(0.95)
                    .lastSeen(Instant.now())
                    .occurrenceCount(150)
                    .build();

            assertThat(pattern.getPatternName()).contains("Velocity");
            assertThat(pattern.getConfidenceScore()).isGreaterThan(0.9);
            assertThat(pattern.getOccurrenceCount()).isGreaterThan(100);
        }

        @Test
        @DisplayName("Should build location anomaly pattern")
        void shouldBuildLocationAnomalyPattern() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("location-001")
                    .patternName("Location Anomaly")
                    .description("Transaction from unusual location")
                    .confidenceScore(0.85)
                    .lastSeen(Instant.now())
                    .occurrenceCount(75)
                    .build();

            assertThat(pattern.getPatternName()).contains("Location");
            assertThat(pattern.getConfidenceScore()).isGreaterThan(0.8);
        }

        @Test
        @DisplayName("Should build amount spike pattern")
        void shouldBuildAmountSpikePattern() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("amount-001")
                    .patternName("Amount Spike")
                    .description("Unusual large transaction amount")
                    .confidenceScore(0.88)
                    .lastSeen(Instant.now())
                    .occurrenceCount(45)
                    .build();

            assertThat(pattern.getPatternName()).contains("Amount");
            assertThat(pattern.getConfidenceScore()).isGreaterThan(0.85);
        }

        @Test
        @DisplayName("Should build device fingerprint pattern")
        void shouldBuildDeviceFingerprintPattern() {
            FraudPattern pattern = FraudPattern.builder()
                    .patternId("device-001")
                    .patternName("Device Fingerprint Mismatch")
                    .description("Transaction from unrecognized device")
                    .confidenceScore(0.78)
                    .lastSeen(Instant.now())
                    .occurrenceCount(30)
                    .build();

            assertThat(pattern.getPatternName()).contains("Device");
            assertThat(pattern.getConfidenceScore()).isGreaterThan(0.7);
        }
    }
}

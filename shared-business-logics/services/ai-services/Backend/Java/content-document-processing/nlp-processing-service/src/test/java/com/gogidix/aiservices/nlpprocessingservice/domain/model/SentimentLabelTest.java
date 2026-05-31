package com.gogidix.aiservices.nlpprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SentimentLabel Domain Model Tests")
class SentimentLabelTest {

    @Nested
    @DisplayName("Sentiment Type Tests")
    class SentimentTypeTests {

        @Test
        @DisplayName("Should have POSITIVE sentiment")
        void shouldHavePositiveSentiment() {
            assertThat(SentimentLabel.POSITIVE).isNotNull();
            assertThat(SentimentLabel.POSITIVE.getDisplayName()).isEqualTo("Positive");
            assertThat(SentimentLabel.POSITIVE.getScore()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should have NEUTRAL sentiment")
        void shouldHaveNeutralSentiment() {
            assertThat(SentimentLabel.NEUTRAL).isNotNull();
            assertThat(SentimentLabel.NEUTRAL.getDisplayName()).isEqualTo("Neutral");
            assertThat(SentimentLabel.NEUTRAL.getScore()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should have NEGATIVE sentiment")
        void shouldHaveNegativeSentiment() {
            assertThat(SentimentLabel.NEGATIVE).isNotNull();
            assertThat(SentimentLabel.NEGATIVE.getDisplayName()).isEqualTo("Negative");
            assertThat(SentimentLabel.NEGATIVE.getScore()).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("Score Classification Tests")
    class ScoreClassificationTests {

        @ParameterizedTest
        @CsvSource({
            "0.3, POSITIVE",
            "0.5, POSITIVE",
            "1.0, POSITIVE",
            "2.0, POSITIVE"
        })
        @DisplayName("Should classify positive scores")
        void shouldClassifyPositiveScores(double score, SentimentLabel expected) {
            assertThat(SentimentLabel.fromScore(score)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "-0.3, NEGATIVE",
            "-0.5, NEGATIVE",
            "-1.0, NEGATIVE",
            "-2.0, NEGATIVE"
        })
        @DisplayName("Should classify negative scores")
        void shouldClassifyNegativeScores(double score, SentimentLabel expected) {
            assertThat(SentimentLabel.fromScore(score)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0.0, NEUTRAL",
            "0.1, NEUTRAL",
            "-0.1, NEUTRAL",
            "0.2, NEUTRAL",
            "-0.2, NEUTRAL"
        })
        @DisplayName("Should classify neutral scores")
        void shouldClassifyNeutralScores(double score, SentimentLabel expected) {
            assertThat(SentimentLabel.fromScore(score)).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should classify boundary positive")
        void shouldClassifyBoundaryPositive() {
            assertThat(SentimentLabel.fromScore(0.21)).isEqualTo(SentimentLabel.POSITIVE);
        }

        @Test
        @DisplayName("Should classify boundary negative")
        void shouldClassifyBoundaryNegative() {
            assertThat(SentimentLabel.fromScore(-0.21)).isEqualTo(SentimentLabel.NEGATIVE);
        }

        @Test
        @DisplayName("Should classify boundary neutral")
        void shouldClassifyBoundaryNeutral() {
            assertThat(SentimentLabel.fromScore(0.2)).isEqualTo(SentimentLabel.NEUTRAL);
            assertThat(SentimentLabel.fromScore(-0.2)).isEqualTo(SentimentLabel.NEUTRAL);
        }
    }

    @Nested
    @DisplayName("Sentiment Properties Tests")
    class SentimentPropertiesTests {

        @Test
        @DisplayName("Should have display names")
        void shouldHaveDisplayNames() {
            assertThat(SentimentLabel.POSITIVE.getDisplayName()).isEqualTo("Positive");
            assertThat(SentimentLabel.NEUTRAL.getDisplayName()).isEqualTo("Neutral");
            assertThat(SentimentLabel.NEGATIVE.getDisplayName()).isEqualTo("Negative");
        }

        @Test
        @DisplayName("Should have numeric scores")
        void shouldHaveNumericScores() {
            assertThat(SentimentLabel.POSITIVE.getScore()).isEqualTo(1);
            assertThat(SentimentLabel.NEUTRAL.getScore()).isEqualTo(0);
            assertThat(SentimentLabel.NEGATIVE.getScore()).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should have three sentiment types")
        void shouldHaveThreeSentimentTypes() {
            assertThat(SentimentLabel.values()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Practical Usage Tests")
    class PracticalUsageTests {

        @Test
        @DisplayName("Should handle very positive text")
        void shouldHandleVeryPositiveText() {
            assertThat(SentimentLabel.fromScore(0.95)).isEqualTo(SentimentLabel.POSITIVE);
        }

        @Test
        @DisplayName("Should handle very negative text")
        void shouldHandleVeryNegativeText() {
            assertThat(SentimentLabel.fromScore(-0.95)).isEqualTo(SentimentLabel.NEGATIVE);
        }

        @Test
        @DisplayName("Should handle completely neutral text")
        void shouldHandleCompletelyNeutralText() {
            assertThat(SentimentLabel.fromScore(0.0)).isEqualTo(SentimentLabel.NEUTRAL);
        }

        @Test
        @DisplayName("Should handle extreme positive score")
        void shouldHandleExtremePositiveScore() {
            assertThat(SentimentLabel.fromScore(10.0)).isEqualTo(SentimentLabel.POSITIVE);
        }

        @Test
        @DisplayName("Should handle extreme negative score")
        void shouldHandleExtremeNegativeScore() {
            assertThat(SentimentLabel.fromScore(-10.0)).isEqualTo(SentimentLabel.NEGATIVE);
        }
    }

    @Nested
    @DisplayName("Sentiment Comparison Tests")
    class SentimentComparisonTests {

        @Test
        @DisplayName("Should be equal when same sentiment")
        void shouldBeEqualWhenSameSentiment() {
            assertThat(SentimentLabel.POSITIVE).isEqualTo(SentimentLabel.POSITIVE);
        }

        @Test
        @DisplayName("Should not be equal when different sentiment")
        void shouldNotBeEqualWhenDifferentSentiment() {
            assertThat(SentimentLabel.POSITIVE).isNotEqualTo(SentimentLabel.NEGATIVE);
            assertThat(SentimentLabel.NEUTRAL).isNotEqualTo(SentimentLabel.POSITIVE);
        }

        @Test
        @DisplayName("Should have ordinal order")
        void shouldHaveOrdinalOrder() {
            assertThat(SentimentLabel.NEGATIVE.ordinal()).isLessThan(SentimentLabel.NEUTRAL.ordinal());
            assertThat(SentimentLabel.NEUTRAL.ordinal()).isLessThan(SentimentLabel.POSITIVE.ordinal());
        }
    }
}

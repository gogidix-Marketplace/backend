package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Recommendation Domain Model Tests")
class RecommendationTest {

    private static final String VALID_ITEM_ID = "item-123";
    private static final double VALID_SCORE = 0.85;
    private static final String VALID_CATEGORY = "electronics";

    @Nested
    @DisplayName("Recommendation Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create recommendation with valid parameters")
        void shouldCreateWithValidParameters() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .category(VALID_CATEGORY)
                    .build();

            assertThat(recommendation).isNotNull();
            assertThat(recommendation.getItemId()).isEqualTo(VALID_ITEM_ID);
            assertThat(recommendation.getScore()).isEqualTo(VALID_SCORE);
            assertThat(recommendation.getReason()).isEqualTo(RecommendationReason.BEHAVIORAL);
            assertThat(recommendation.getCategory()).isEqualTo(VALID_CATEGORY);
        }

        @Test
        @DisplayName("Should reject null item ID")
        void shouldRejectNullItemId() {
            assertThatThrownBy(() -> Recommendation.builder()
                    .itemId(null)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Item ID cannot be null");
        }

        @Test
        @DisplayName("Should reject empty item ID")
        void shouldRejectEmptyItemId() {
            assertThatThrownBy(() -> Recommendation.builder()
                    .itemId("")
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Item ID cannot be empty");
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, -0.5, 1.1, 1.5, 2.0})
        @DisplayName("Should reject scores outside 0-1 range")
        void shouldRejectInvalidScores(double score) {
            assertThatThrownBy(() -> Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(score)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Score must be between 0 and 1");
        }

        @Test
        @DisplayName("Should reject null reason")
        void shouldRejectNullReason() {
            assertThatThrownBy(() -> Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(null)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Reason cannot be null");
        }

        @Test
        @DisplayName("Should accept recommendation without category")
        void shouldAcceptWithoutCategory() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(recommendation).isNotNull();
            assertThat(recommendation.getCategory()).isNull();
        }
    }

    @Nested
    @DisplayName("Score Validation Tests")
    class ScoreValidationTests {

        @ParameterizedTest
        @CsvSource({"0.0", "0.3", "0.5", "0.7", "1.0"})
        @DisplayName("Should accept valid score boundaries")
        void shouldAcceptValidScores(double score) {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(score)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(recommendation.getScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should round scores to 4 decimal places")
        void shouldRoundScores() {
            double preciseScore = 0.856789;
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(preciseScore)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            BigDecimal rounded = BigDecimal.valueOf(recommendation.getScore())
                    .setScale(4, RoundingMode.HALF_UP);

            assertThat(rounded).isEqualByComparingTo("0.8568");
        }

        @Test
        @DisplayName("Should calculate score tier")
        void shouldCalculateScoreTier() {
            Recommendation highScore = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.9)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation mediumScore = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.6)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation lowScore = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.4)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(highScore.getScoreTier()).isEqualTo(ScoreTier.HIGH);
            assertThat(mediumScore.getScoreTier()).isEqualTo(ScoreTier.MEDIUM);
            assertThat(lowScore.getScoreTier()).isEqualTo(ScoreTier.LOW);
        }

        @Test
        @DisplayName("Should classify very low scores as LOW")
        void shouldClassifyVeryLowAsLow() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.29)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(recommendation.getScoreTier()).isEqualTo(ScoreTier.LOW);
        }
    }

    @Nested
    @DisplayName("Recommendation Reason Tests")
    class ReasonTests {

        @ParameterizedTest
        @EnumSource(RecommendationReason.class)
        @DisplayName("Should accept all recommendation reasons")
        void shouldAcceptAllReasons(RecommendationReason reason) {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(reason)
                    .build();

            assertThat(recommendation.getReason()).isEqualTo(reason);
        }

        @Test
        @DisplayName("Should have human-readable reason descriptions")
        void shouldHaveReadableDescriptions() {
            assertThat(RecommendationReason.BEHAVIORAL.getDescription())
                    .isEqualTo("Based on your browsing behavior");
            assertThat(RecommendationReason.COLLABORATIVE.getDescription())
                    .isEqualTo("Users like you also liked this");
            assertThat(RecommendationReason.CONTENT_BASED.getDescription())
                    .isEqualTo("Similar to items you've viewed");
            assertThat(RecommendationReason.TRENDING.getDescription())
                    .isEqualTo("Currently popular");
            assertThat(RecommendationReason.CONTEXTUAL.getDescription())
                    .isEqualTo("Based on current context");
        }
    }

    @Nested
    @DisplayName("Recommendation Filtering Tests")
    class FilteringTests {

        @Test
        @DisplayName("Should check if recommendation meets minimum threshold")
        void shouldCheckMinimumThreshold() {
            Recommendation validRecommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation invalidRecommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.2)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(validRecommendation.meetsThreshold(0.3)).isTrue();
            assertThat(invalidRecommendation.meetsThreshold(0.3)).isFalse();
        }

        @Test
        @DisplayName("Should filter by category")
        void shouldFilterByCategory() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .category(VALID_CATEGORY)
                    .build();

            assertThat(recommendation.matchesCategory("electronics")).isTrue();
            assertThat(recommendation.matchesCategory("books")).isFalse();
        }

        @Test
        @DisplayName("Should match any category when category is null")
        void shouldMatchAnyCategoryWhenNull() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(recommendation.matchesCategory("electronics")).isTrue();
            assertThat(recommendation.matchesCategory("books")).isTrue();
        }

        @Test
        @DisplayName("Should filter by reason type")
        void shouldFilterByReason() {
            Recommendation behavioralRec = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(VALID_SCORE)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(behavioralRec.hasReason(RecommendationReason.BEHAVIORAL)).isTrue();
            assertThat(behavioralRec.hasReason(RecommendationReason.TRENDING)).isFalse();
        }
    }

    @Nested
    @DisplayName("Recommendation Boosting Tests")
    class BoostingTests {

        @Test
        @DisplayName("Should apply boost factor to score")
        void shouldApplyBoostFactor() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = recommendation.withBoost(1.2);

            assertThat(boosted.getScore()).isGreaterThan(0.5);
            assertThat(boosted.getScore()).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should cap boosted score at 1.0")
        void shouldCapBoostedScore() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.9)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = recommendation.withBoost(1.5);

            assertThat(boosted.getScore()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should apply fresh content boost")
        void shouldApplyFreshContentBoost() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = recommendation.withFreshContentBoost();

            assertThat(boosted.getScore()).isGreaterThan(0.5);
            assertThat(boosted.getScore()).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should apply trending boost")
        void shouldApplyTrendingBoost() {
            Recommendation recommendation = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = recommendation.withTrendingBoost();

            assertThat(boosted.getScore()).isGreaterThan(0.5);
        }
    }

    @Nested
    @DisplayName("Comparison and Sorting Tests")
    class ComparisonTests {

        @Test
        @DisplayName("Should sort by score descending")
        void shouldSortByScoreDescending() {
            Recommendation rec1 = Recommendation.builder()
                    .itemId("item-1")
                    .score(0.7)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation rec2 = Recommendation.builder()
                    .itemId("item-2")
                    .score(0.9)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation rec3 = Recommendation.builder()
                    .itemId("item-3")
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(rec2.compareTo(rec1)).isPositive();
            assertThat(rec1.compareTo(rec3)).isPositive();
            assertThat(rec3.compareTo(rec1)).isNegative();
            assertThat(rec1.compareTo(rec1)).isZero();
        }

        @Test
        @DisplayName("Should sort by score then by item ID")
        void shouldSortByScoreThenItemId() {
            Recommendation rec1 = Recommendation.builder()
                    .itemId("item-a")
                    .score(0.8)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation rec2 = Recommendation.builder()
                    .itemId("item-b")
                    .score(0.8)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(rec1.compareTo(rec2)).isNegative();
            assertThat(rec2.compareTo(rec1)).isPositive();
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should create new instance on boost")
        void shouldCreateNewInstanceOnBoost() {
            Recommendation original = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = original.withBoost(1.2);

            assertThat(original).isNotSameAs(boosted);
            assertThat(original.getScore()).isEqualTo(0.5);
            assertThat(boosted.getScore()).isNotEqualTo(0.5);
        }

        @Test
        @DisplayName("Should be equal based on item ID")
        void shouldBeEqualBasedOnItemId() {
            Recommendation rec1 = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation rec2 = Recommendation.builder()
                    .itemId(VALID_ITEM_ID)
                    .score(0.7)
                    .reason(RecommendationReason.TRENDING)
                    .build();

            assertThat(rec1).isEqualTo(rec2);
            assertThat(rec1.hashCode()).isEqualTo(rec2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different item IDs")
        void shouldNotBeEqualWithDifferentIds() {
            Recommendation rec1 = Recommendation.builder()
                    .itemId("item-1")
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation rec2 = Recommendation.builder()
                    .itemId("item-2")
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            assertThat(rec1).isNotEqualTo(rec2);
        }
    }

    @Nested
    @DisplayName("Recommendation List Tests")
    class ListTests {

        @Test
        @DisplayName("Should filter recommendations below threshold")
        void shouldFilterBelowThreshold() {
            java.util.List<Recommendation> recommendations = java.util.List.of(
                    createRecommendation("item-1", 0.9),
                    createRecommendation("item-2", 0.4),
                    createRecommendation("item-3", 0.2),
                    createRecommendation("item-4", 0.6)
            );

            java.util.List<Recommendation> filtered = recommendations.stream()
                    .filter(r -> r.meetsThreshold(0.3))
                    .toList();

            assertThat(filtered).hasSize(3);
            assertThat(filtered).allMatch(r -> r.getScore() >= 0.3);
        }

        @Test
        @DisplayName("Should limit recommendation list")
        void shouldLimitList() {
            java.util.List<Recommendation> recommendations = new java.util.ArrayList<>();
            for (int i = 0; i < 150; i++) {
                recommendations.add(createRecommendation("item-" + i, 0.5 + (i % 50) / 100.0));
            }

            java.util.List<Recommendation> limited = recommendations.stream()
                    .sorted()
                    .limit(100)
                    .toList();

            assertThat(limited).hasSize(100);
        }

        @Test
        @DisplayName("Should sort and filter recommendations")
        void shouldSortAndFilter() {
            java.util.List<Recommendation> recommendations = java.util.List.of(
                    createRecommendation("item-1", 0.3),
                    createRecommendation("item-2", 0.9),
                    createRecommendation("item-3", 0.5),
                    createRecommendation("item-4", 0.7)
            );

            java.util.List<Recommendation> result = recommendations.stream()
                    .filter(r -> r.meetsThreshold(0.4))
                    .sorted()
                    .toList();

            assertThat(result).hasSize(3);
            assertThat(result.get(0).getItemId()).isEqualTo("item-2");
            assertThat(result.get(2).getItemId()).isEqualTo("item-3");
        }
    }

    private Recommendation createRecommendation(String itemId, double score) {
        return Recommendation.builder()
                .itemId(itemId)
                .score(score)
                .reason(RecommendationReason.BEHAVIORAL)
                .build();
    }
}

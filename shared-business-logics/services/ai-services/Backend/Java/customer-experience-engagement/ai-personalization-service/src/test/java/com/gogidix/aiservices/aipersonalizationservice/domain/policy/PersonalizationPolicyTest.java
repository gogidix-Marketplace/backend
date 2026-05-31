package com.gogidix.aiservices.aipersonalizationservice.domain.policy;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Personalization Policy Tests")
class PersonalizationPolicyTest {

    @Nested
    @DisplayName("Segmentation Policy Tests")
    class SegmentationPolicyTests {

        @Test
        @DisplayName("Should assign NEW_USER segment for new profiles")
        void shouldAssignNewUserSegment() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(Segment.NEW_USER);
        }

        @Test
        @DisplayName("Should assign ACTIVE segment with sufficient interactions")
        void shouldAssignActiveSegment() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(15, 0);

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(Segment.ACTIVE);
        }

        @Test
        @DisplayName("Should assign VIP segment with high value")
        void shouldAssignVipSegment() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(100, 15);

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(Segment.VIP);
        }

        @Test
        @DisplayName("Should assign INACTIVE segment for dormant users")
        void shouldAssignInactiveSegment() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            // Add old interaction
            Instant oldTimestamp = Instant.now().minusSeconds(31L * 24 * 60 * 60);
            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", oldTimestamp));

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(Segment.INACTIVE);
        }

        @Test
        @DisplayName("Should assign CHURNED segment for very inactive users")
        void shouldAssignChurnedSegment() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(20, 2);

            // Make all interactions old
            Instant oldTimestamp = Instant.now().minusSeconds(91L * 24 * 60 * 60);
            profile.getBehaviorHistory().clear();
            for (int i = 0; i < 20; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, oldTimestamp));
            }

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(Segment.CHURNED);
        }

        @ParameterizedTest
        @CsvSource({
                "5, 0, NEW_USER",
                "15, 0, ACTIVE",
                "100, 5, ACTIVE",
                "100, 10, VIP",
                "150, 15, VIP"
        })
        @DisplayName("Should correctly segment based on interactions and purchases")
        void shouldSegmentCorrectly(int interactions, int purchases, Segment expected) {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(interactions, purchases);

            Segment segment = policy.determineSegment(profile);

            assertThat(segment).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Affinity Calculation Policy Tests")
    class AffinityPolicyTests {

        @Test
        @DisplayName("Should calculate category affinity from behaviors")
        void shouldCalculateCategoryAffinity() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            // Add technology category interactions
            for (int i = 0; i < 10; i++) {
                BehaviorEvent event = createEvent(EventType.VIEW, "tech-item-" + i, Instant.now());
                event.addProperties("category", "technology");
                profile.addBehaviorEvent(event);
            }

            // Add sports category interactions
            for (int i = 0; i < 3; i++) {
                BehaviorEvent event = createEvent(EventType.VIEW, "sport-item-" + i, Instant.now());
                event.addProperties("category", "sports");
                profile.addBehaviorEvent(event);
            }

            var affinities = policy.calculateAffinityScores(profile);

            assertThat(affinities.get("technology")).isGreaterThan(affinities.get("sports"));
        }

        @Test
        @DisplayName("Should apply different weights to event types")
        void shouldApplyEventWeights() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            // Add one purchase
            BehaviorEvent purchaseEvent = createEvent(EventType.PURCHASE, "item-1", Instant.now());
            purchaseEvent.addProperties("category", "electronics");
            profile.addBehaviorEvent(purchaseEvent);

            // Add 5 views
            for (int i = 0; i < 5; i++) {
                BehaviorEvent viewEvent = createEvent(EventType.VIEW, "item-" + (i + 2), Instant.now());
                viewEvent.addProperties("category", "electronics");
                profile.addBehaviorEvent(viewEvent);
            }

            var affinities = policy.calculateAffinityScores(profile);

            // Purchase should have significant weight
            assertThat(affinities.get("electronics")).isGreaterThan(0.3);
        }

        @Test
        @DisplayName("Should decay old interactions")
        void shouldDecayOldInteractions() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            // Add recent interaction
            BehaviorEvent recentEvent = createEvent(EventType.VIEW, "item-1", Instant.now());
            recentEvent.addProperties("category", "recent");
            profile.addBehaviorEvent(recentEvent);

            // Add old interaction
            BehaviorEvent oldEvent = createEvent(EventType.VIEW, "item-2", Instant.now().minusSeconds(30L * 24 * 60 * 60));
            oldEvent.addProperties("category", "old");
            profile.addBehaviorEvent(oldEvent);

            var affinities = policy.calculateAffinityScores(profile);

            assertThat(affinities.get("recent")).isGreaterThan(affinities.get("old"));
        }

        @Test
        @DisplayName("Should normalize scores to 0-1 range")
        void shouldNormalizeScores() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(50, 5);

            var affinities = policy.calculateAffinityScores(profile);

            affinities.values().forEach(score -> {
                assertThat(score).isBetween(0.0, 1.0);
            });
        }
    }

    @Nested
    @DisplayName("Recommendation Eligibility Policy Tests")
    class EligibilityPolicyTests {

        @Test
        @DisplayName("Should require minimum interactions for recommendations")
        void shouldRequireMinimumInteractions() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            assertThat(policy.isEligibleForRecommendations(profile)).isFalse();

            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", Instant.now()));
            assertThat(policy.isEligibleForRecommendations(profile)).isFalse();

            for (int i = 1; i < 5; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }
            assertThat(policy.isEligibleForRecommendations(profile)).isTrue();
        }

        @Test
        @DisplayName("Should check minimum affinity threshold")
        void shouldCheckAffinityThreshold() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = createProfileWithInteractions(10, 0);

            assertThat(policy.hasMinimumAffinity(profile)).isTrue();
        }

        @Test
        @DisplayName("Should not be eligible without affinity data")
        void shouldNotBeEligibleWithoutAffinity() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            assertThat(policy.hasMinimumAffinity(profile)).isFalse();
        }
    }

    @Nested
    @DisplayName("Content Filtering Policy Tests")
    class ContentFilteringPolicyTests {

        @Test
        @DisplayName("Should filter out low-score recommendations")
        void shouldFilterLowScores() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            List<Recommendation> recommendations = Arrays.asList(
                    Recommendation.builder().itemId("item-1").score(0.9).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-2").score(0.2).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-3").score(0.5).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-4").score(0.25).reason(RecommendationReason.BEHAVIORAL).build()
            );

            List<Recommendation> filtered = policy.filterByScoreThreshold(recommendations, 0.3);

            assertThat(filtered).hasSize(2);
            assertThat(filtered).allMatch(r -> r.getScore() >= 0.3);
        }

        @Test
        @DisplayName("Should filter by category preferences")
        void shouldFilterByCategory() {
            PersonalizationPolicy policy = new PersonalizationPolicy();
            UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

            UserAttributes attributes = UserAttributes.builder()
                    .preferences(Preferences.builder()
                            .categories(Arrays.asList("electronics", "books"))
                            .build())
                    .build();
            profile.updateAttributes(attributes);

            List<Recommendation> recommendations = Arrays.asList(
                    Recommendation.builder().itemId("item-1").score(0.9).reason(RecommendationReason.BEHAVIORAL).category("electronics").build(),
                    Recommendation.builder().itemId("item-2").score(0.7).reason(RecommendationReason.BEHAVIORAL).category("sports").build(),
                    Recommendation.builder().itemId("item-3").score(0.6).reason(RecommendationReason.BEHAVIORAL).category("books").build()
            );

            List<Recommendation> filtered = policy.filterByUserPreferences(recommendations, profile);

            assertThat(filtered).hasSize(2);
            assertThat(filtered).allMatch(r -> profile.getAttributes().getPreferences().getCategories().contains(r.getCategory()));
        }

        @Test
        @DisplayName("Should limit recommendation count")
        void shouldLimitCount() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            List<Recommendation> recommendations = new java.util.ArrayList<>();
            for (int i = 0; i < 150; i++) {
                recommendations.add(Recommendation.builder()
                        .itemId("item-" + i)
                        .score(0.5 + (i % 50) / 100.0)
                        .reason(RecommendationReason.BEHAVIORAL)
                        .build());
            }

            List<Recommendation> limited = policy.limitRecommendations(recommendations, 50);

            assertThat(limited).hasSize(50);
        }

        @Test
        @DisplayName("Should not exceed max limit")
        void shouldNotExceedMaxLimit() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            List<Recommendation> recommendations = new java.util.ArrayList<>();
            for (int i = 0; i < 200; i++) {
                recommendations.add(Recommendation.builder()
                        .itemId("item-" + i)
                        .score(0.9)
                        .reason(RecommendationReason.BEHAVIORAL)
                        .build());
            }

            List<Recommendation> limited = policy.limitRecommendations(recommendations, 150);

            assertThat(limited).hasSize(100); // Max limit is 100
        }
    }

    @Nested
    @DisplayName("Diversity Policy Tests")
    class DiversityPolicyTests {

        @Test
        @DisplayName("Should ensure category diversity in recommendations")
        void shouldEnsureDiversity() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            List<Recommendation> recommendations = Arrays.asList(
                    Recommendation.builder().itemId("item-1").score(0.9).reason(RecommendationReason.BEHAVIORAL).category("electronics").build(),
                    Recommendation.builder().itemId("item-2").score(0.85).reason(RecommendationReason.BEHAVIORAL).category("electronics").build(),
                    Recommendation.builder().itemId("item-3").score(0.8).reason(RecommendationReason.BEHAVIORAL).category("electronics").build(),
                    Recommendation.builder().itemId("item-4").score(0.75).reason(RecommendationReason.BEHAVIORAL).category("books").build(),
                    Recommendation.builder().itemId("item-5").score(0.7).reason(RecommendationReason.BEHAVIORAL).category("books").build()
            );

            List<Recommendation> diversified = policy.applyDiversity(recommendations, 2);

            long uniqueCategories = diversified.stream()
                    .map(Recommendation::getCategory)
                    .distinct()
                    .count();

            assertThat(uniqueCategories).isGreaterThanOrEqualTo(2);
        }

        @Test
        @DisplayName("Should mix recommendation reasons")
        void shouldMixReasons() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            List<Recommendation> recommendations = Arrays.asList(
                    Recommendation.builder().itemId("item-1").score(0.9).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-2").score(0.85).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-3").score(0.8).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-4").score(0.6).reason(RecommendationReason.TRENDING).build(),
                    Recommendation.builder().itemId("item-5").score(0.55).reason(RecommendationReason.TRENDING).build()
            );

            List<Recommendation> diversified = policy.applyReasonDiversity(recommendations);

            long uniqueReasons = diversified.stream()
                    .map(Recommendation::getReason)
                    .distinct()
                    .count();

            assertThat(uniqueReasons).isGreaterThan(1);
        }
    }

    @Nested
    @DisplayName("Boost Policy Tests")
    class BoostPolicyTests {

        @Test
        @DisplayName("Should apply fresh content boost")
        void shouldApplyFreshContentBoost() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            Recommendation recommendation = Recommendation.builder()
                    .itemId("item-1")
                    .score(0.5)
                    .reason(RecommendationReason.BEHAVIORAL)
                    .build();

            Recommendation boosted = policy.applyFreshContentBoost(recommendation);

            assertThat(boosted.getScore()).isGreaterThan(0.5);
            assertThat(boosted.getScore()).isLessThanOrEqualTo(1.0);
        }

        @Test
        @DisplayName("Should apply trending boost")
        void shouldApplyTrendingBoost() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            Recommendation recommendation = Recommendation.builder()
                    .itemId("trending-item")
                    .score(0.6)
                    .reason(RecommendationReason.TRENDING)
                    .build();

            Recommendation boosted = policy.applyTrendingBoost(recommendation);

            assertThat(boosted.getScore()).isGreaterThan(0.6);
        }

        @Test
        @DisplayName("Should apply context-aware boost")
        void shouldApplyContextBoost() {
            PersonalizationPolicy policy = new PersonalizationPolicy();

            Recommendation recommendation = Recommendation.builder()
                    .itemId("contextual-item")
                    .score(0.5)
                    .reason(RecommendationReason.CONTEXTUAL)
                    .build();

            Recommendation boosted = policy.applyContextualBoost(recommendation, "search");

            assertThat(boosted.getScore()).isGreaterThan(0.5);
        }
    }

    // Helper methods

    private UserProfile createProfileWithInteractions(int interactions, int purchases) {
        UserProfile profile = UserProfile.create("550e8400-e29b-41d4-a716-446655440000");

        for (int i = 0; i < interactions; i++) {
            EventType type = i < purchases ? EventType.PURCHASE : EventType.VIEW;
            String itemId = type == EventType.PURCHASE ? "product-" + i : "item-" + i;
            profile.addBehaviorEvent(createEvent(type, itemId, Instant.now()));
        }

        return profile;
    }

    private BehaviorEvent createEvent(EventType type, String itemId, Instant timestamp) {
        BehaviorEvent event = BehaviorEvent.builder()
                .eventType(type)
                .itemId(itemId)
                .timestamp(timestamp)
                .build();
        return event;
    }
}

package com.gogidix.aiservices.airecommendationservice.domain.policy;

import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationItem;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationReason;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RecommendationPolicy Business Logic Tests")
class RecommendationPolicyTest {

    private RecommendationPolicy policy;
    private List<RecommendationItem> testItems;

    @BeforeEach
    void setUp() {
        policy = new RecommendationPolicy();

        // Create test items with varying scores and categories
        testItems = new ArrayList<>();
        testItems.add(createItem("item-1", "electronics", 0.95, RecommendationReason.COLLABORATIVE));
        testItems.add(createItem("item-2", "electronics", 0.85, RecommendationReason.CONTENT_BASED));
        testItems.add(createItem("item-3", "electronics", 0.75, RecommendationReason.BEHAVIORAL));
        testItems.add(createItem("item-4", "books", 0.90, RecommendationReason.COLLABORATIVE));
        testItems.add(createItem("item-5", "books", 0.65, RecommendationReason.TRENDING));
        testItems.add(createItem("item-6", "books", 0.55, RecommendationReason.CONTEXTUAL));
        testItems.add(createItem("item-7", "clothing", 0.25, RecommendationReason.CONTENT_BASED));
        testItems.add(createItem("item-8", "clothing", 0.45, RecommendationReason.BEHAVIORAL));
        testItems.add(createItem("item-9", null, 0.80, RecommendationReason.TRENDING));
        testItems.add(createItem("item-10", "default", 0.35, RecommendationReason.CONTEXTUAL));
    }

    private RecommendationItem createItem(String id, String category, double score, RecommendationReason reason) {
        return RecommendationItem.builder()
                .itemId(id)
                .title("Item " + id)
                .category(category)
                .score(score)
                .reason(reason)
                .build();
    }

    @Nested
    @DisplayName("filterByScore Tests")
    class FilterByScoreTests {

        @Test
        @DisplayName("Should filter items above threshold")
        void shouldFilterItemsAboveThreshold() {
            List<RecommendationItem> filtered = policy.filterByScore(testItems, 0.5);

            assertThat(filtered).hasSize(7);
            assertThat(filtered).allMatch(item -> item.getScore() >= 0.5);
            assertThat(filtered).noneMatch(item -> item.getScore() < 0.5);
        }

        @Test
        @DisplayName("Should filter with default threshold")
        void shouldFilterWithDefaultThreshold() {
            List<RecommendationItem> filtered = policy.filterByScore(testItems, 0.3);

            assertThat(filtered).hasSize(9);
            assertThat(filtered).noneMatch(item -> item.getScore() < 0.3);
        }

        @Test
        @DisplayName("Should return empty list when no items meet threshold")
        void shouldReturnEmptyListWhenNoItemsMeetThreshold() {
            List<RecommendationItem> filtered = policy.filterByScore(testItems, 0.99);

            assertThat(filtered).isEmpty();
        }

        @Test
        @DisplayName("Should return all items when threshold is zero")
        void shouldReturnAllItemsWhenThresholdIsZero() {
            List<RecommendationItem> filtered = policy.filterByScore(testItems, 0.0);

            assertThat(filtered).hasSize(10);
        }

        @Test
        @DisplayName("Should include items exactly at threshold")
        void shouldIncludeItemsAtThreshold() {
            List<RecommendationItem> items = List.of(
                    createItem("item-1", "cat", 0.5, RecommendationReason.CONTENT_BASED),
                    createItem("item-2", "cat", 0.51, RecommendationReason.CONTENT_BASED),
                    createItem("item-3", "cat", 0.49, RecommendationReason.CONTENT_BASED)
            );

            List<RecommendationItem> filtered = policy.filterByScore(items, 0.5);

            assertThat(filtered).hasSize(2);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<RecommendationItem> filtered = policy.filterByScore(List.of(), 0.5);

            assertThat(filtered).isEmpty();
        }
    }

    @Nested
    @DisplayName("applyDiversity Tests")
    class ApplyDiversityTests {

        @Test
        @DisplayName("Should sort items by score descending")
        void shouldSortItemsByScoreDescending() {
            List<RecommendationItem> diverse = policy.applyDiversity(testItems);

            assertThat(diverse).isNotEmpty();
            for (int i = 0; i < diverse.size() - 1; i++) {
                assertThat(diverse.get(i).getScore())
                        .isGreaterThanOrEqualTo(diverse.get(i + 1).getScore());
            }
        }

        @Test
        @DisplayName("Should limit to max recommendations")
        void shouldLimitToMaxRecommendations() {
            // Create more than MAX_RECOMMENDATIONS (100) items
            List<RecommendationItem> manyItems = new ArrayList<>();
            for (int i = 0; i < 150; i++) {
                manyItems.add(createItem("item-" + i, "cat", 0.9 - (i * 0.001), RecommendationReason.CONTENT_BASED));
            }

            List<RecommendationItem> limited = policy.applyDiversity(manyItems);

            assertThat(limited).hasSize(100);
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<RecommendationItem> diverse = policy.applyDiversity(List.of());

            assertThat(diverse).isEmpty();
        }

        @Test
        @DisplayName("Should handle single item")
        void shouldHandleSingleItem() {
            List<RecommendationItem> single = List.of(
                    createItem("item-1", "cat", 0.5, RecommendationReason.CONTENT_BASED)
            );

            List<RecommendationItem> diverse = policy.applyDiversity(single);

            assertThat(diverse).hasSize(1);
        }
    }

    @Nested
    @DisplayName("limitByCategory Tests")
    class LimitByCategoryTests {

        @Test
        @DisplayName("Should limit items per category")
        void shouldLimitItemsPerCategory() {
            // Add more items to electronics and books
            List<RecommendationItem> items = new ArrayList<>(testItems);
            for (int i = 0; i < 10; i++) {
                items.add(createItem("elec-" + i, "electronics", 0.9 - (i * 0.05), RecommendationReason.CONTENT_BASED));
                items.add(createItem("book-" + i, "books", 0.9 - (i * 0.05), RecommendationReason.CONTENT_BASED));
            }

            List<RecommendationItem> limited = policy.limitByCategory(items, 5);

            long electronicsCount = limited.stream()
                    .filter(item -> "electronics".equals(item.getCategory()))
                    .count();
            long booksCount = limited.stream()
                    .filter(item -> "books".equals(item.getCategory()))
                    .count();

            assertThat(electronicsCount).isLessThanOrEqualTo(5);
            assertThat(booksCount).isLessThanOrEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null category as default")
        void shouldHandleNullCategoryAsDefault() {
            List<RecommendationItem> items = List.of(
                    createItem("item-1", null, 0.9, RecommendationReason.CONTENT_BASED),
                    createItem("item-2", null, 0.8, RecommendationReason.CONTENT_BASED),
                    createItem("item-3", null, 0.7, RecommendationReason.CONTENT_BASED)
            );

            List<RecommendationItem> limited = policy.limitByCategory(items, 2);

            assertThat(limited).hasSize(2);
        }

        @Test
        @DisplayName("Should sort items within category by score")
        void shouldSortItemsWithinCategoryByScore() {
            List<RecommendationItem> items = List.of(
                    createItem("item-1", "cat", 0.5, RecommendationReason.CONTENT_BASED),
                    createItem("item-2", "cat", 0.9, RecommendationReason.CONTENT_BASED),
                    createItem("item-3", "cat", 0.7, RecommendationReason.CONTENT_BASED)
            );

            List<RecommendationItem> limited = policy.limitByCategory(items, 5);

            assertThat(limited.get(0).getScore()).isGreaterThan(limited.get(1).getScore());
        }

        @Test
        @DisplayName("Should handle empty list")
        void shouldHandleEmptyList() {
            List<RecommendationItem> limited = policy.limitByCategory(List.of(), 5);

            assertThat(limited).isEmpty();
        }
    }

    @Nested
    @DisplayName("isValidRecommendationType Tests")
    class IsValidRecommendationTypeTests {

        @Test
        @DisplayName("Should return true for valid types")
        void shouldReturnTrueForValidTypes() {
            assertThat(policy.isValidRecommendationType(RecommendationType.PERSONALIZED)).isTrue();
            assertThat(policy.isValidRecommendationType(RecommendationType.COLLABORATIVE)).isTrue();
            assertThat(policy.isValidRecommendationType(RecommendationType.CONTENT_BASED)).isTrue();
            assertThat(policy.isValidRecommendationType(RecommendationType.HYBRID)).isTrue();
            assertThat(policy.isValidRecommendationType(RecommendationType.POPULAR)).isTrue();
        }

        @Test
        @DisplayName("Should return false for null type")
        void shouldReturnFalseForNullType() {
            assertThat(policy.isValidRecommendationType(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("getMaxRecommendations Tests")
    class GetMaxRecommendationsTests {

        @Test
        @DisplayName("Should return max recommendations limit")
        void shouldReturnMaxRecommendationsLimit() {
            int max = policy.getMaxRecommendations();

            assertThat(max).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("deduplicateResults Tests")
    class DeduplicateResultsTests {

        @Test
        @DisplayName("Should remove duplicate items")
        void shouldRemoveDuplicateItems() {
            List<RecommendationItem> itemsWithDuplicates = List.of(
                    createItem("item-1", "cat", 0.9, RecommendationReason.CONTENT_BASED),
                    createItem("item-2", "cat", 0.8, RecommendationReason.CONTENT_BASED),
                    createItem("item-1", "cat", 0.7, RecommendationReason.COLLABORATIVE),
                    createItem("item-3", "cat", 0.6, RecommendationReason.BEHAVIORAL)
            );

            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(itemsWithDuplicates)
                    .generatedAt(Instant.now())
                    .build();

            RecommendationResult deduplicated = policy.deduplicateResults(result);

            assertThat(deduplicated.getItems()).hasSize(3);
            assertThat(deduplicated.getItems().stream().map(RecommendationItem::getItemId).distinct().count()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should preserve result metadata")
        void shouldPreserveResultMetadata() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.HYBRID)
                    .items(testItems)
                    .sessionId("session-789")
                    .generatedAt(Instant.now())
                    .algorithm("test-algo")
                    .confidence(0.95)
                    .build();

            RecommendationResult deduplicated = policy.deduplicateResults(result);

            assertThat(deduplicated.getRequestId()).isEqualTo("req-123");
            assertThat(deduplicated.getUserId()).isEqualTo("user-456");
            assertThat(deduplicated.getType()).isEqualTo(RecommendationType.HYBRID);
            assertThat(deduplicated.getSessionId()).isEqualTo("session-789");
            assertThat(deduplicated.getAlgorithm()).isEqualTo("test-algo");
            assertThat(deduplicated.getConfidence()).isEqualTo(0.95);
        }

        @Test
        @DisplayName("Should handle empty items list")
        void shouldHandleEmptyItemsList() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(List.of())
                    .generatedAt(Instant.now())
                    .build();

            RecommendationResult deduplicated = policy.deduplicateResults(result);

            assertThat(deduplicated.getItems()).isEmpty();
        }

        @Test
        @DisplayName("Should handle already unique items")
        void shouldHandleAlreadyUniqueItems() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(Instant.now())
                    .build();

            RecommendationResult deduplicated = policy.deduplicateResults(result);

            assertThat(deduplicated.getItems()).hasSize(testItems.size());
        }
    }
}

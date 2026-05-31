package com.gogidix.aiservices.airecommendationservice;

import com.gogidix.aiservices.airecommendationservice.domain.model.*;
import com.gogidix.aiservices.airecommendationservice.domain.policy.RecommendationPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AI Recommendation Service Tests")
class AiRecommendationServiceTest {

    @Nested
    @DisplayName("RecommendationItem Tests")
    class RecommendationItemTest {

        @Test
        @DisplayName("Should create valid item")
        void shouldCreateValidItem() {
            RecommendationItem item = new RecommendationItem("i1", "Widget", "cat", 0.85,
                    RecommendationReason.BEHAVIORAL, Map.of("source", "ai"), null, 9.99);
            assertEquals("i1", item.getItemId());
            assertEquals("Widget", item.getTitle());
            assertEquals("cat", item.getCategory());
            assertEquals(0.85, item.getScore(), 0.001);
            assertEquals(RecommendationReason.BEHAVIORAL, item.getReason());
            assertEquals(9.99, item.getPrice(), 0.001);
        }

        @Test
        @DisplayName("Should default reason to CONTENT_BASED")
        void shouldDefaultReason() {
            RecommendationItem item = new RecommendationItem("i1", "T", "c", 0.5,
                    null, null, null, null);
            assertEquals(RecommendationReason.CONTENT_BASED, item.getReason());
        }

        @Test
        @DisplayName("Should reject null itemId")
        void shouldRejectNullItemId() {
            assertThrows(IllegalArgumentException.class, () ->
                    new RecommendationItem(null, "T", "c", 0.5, null, null, null, null));
        }

        @Test
        @DisplayName("Should reject empty itemId")
        void shouldRejectEmptyItemId() {
            assertThrows(IllegalArgumentException.class, () ->
                    new RecommendationItem("  ", "T", "c", 0.5, null, null, null, null));
        }

        @Test
        @DisplayName("Should reject negative score")
        void shouldRejectNegativeScore() {
            assertThrows(IllegalArgumentException.class, () ->
                    new RecommendationItem("i1", "T", "c", -0.1, null, null, null, null));
        }

        @Test
        @DisplayName("Should reject score > 1")
        void shouldRejectHighScore() {
            assertThrows(IllegalArgumentException.class, () ->
                    new RecommendationItem("i1", "T", "c", 1.5, null, null, null, null));
        }

        @Test
        @DisplayName("Should detect meets threshold")
        void shouldDetectMeetsThreshold() {
            RecommendationItem item = new RecommendationItem("i1", "T", "c", 0.7,
                    null, null, null, null);
            assertTrue(item.meetsThreshold(0.5));
            assertFalse(item.meetsThreshold(0.8));
        }

        @Test
        @DisplayName("Should match category")
        void shouldMatchCategory() {
            RecommendationItem item = new RecommendationItem("i1", "T", "electronics",
                    0.5, null, null, null, null);
            assertTrue(item.matchesCategory("electronics"));
        }

        @Test
        @DisplayName("Should not match different category")
        void shouldNotMatchDifferentCategory() {
            RecommendationItem item = new RecommendationItem("i1", "T", "electronics",
                    0.5, null, null, null, null);
            assertFalse(item.matchesCategory("books"));
        }

        @Test
        @DisplayName("Null category matches everything")
        void nullCategoryMatchesAll() {
            RecommendationItem item = new RecommendationItem("i1", "T", null,
                    0.5, null, null, null, null);
            assertTrue(item.matchesCategory("anything"));
        }

        @Test
        @DisplayName("Should use builder")
        void shouldUseBuilder() {
            RecommendationItem item = RecommendationItem.builder()
                    .itemId("b1").title("Built").category("cat").score(0.9).build();
            assertEquals("b1", item.getItemId());
        }

        @Test
        @DisplayName("Equals based on itemId")
        void equalsBasedOnItemId() {
            RecommendationItem a = new RecommendationItem("i1", "A", "c1", 0.5, null, null, null, null);
            RecommendationItem b = new RecommendationItem("i1", "B", "c2", 0.9, null, null, null, null);
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Nested
    @DisplayName("RecommendationType Tests")
    class RecommendationTypeTest {

        @Test
        @DisplayName("Should have 5 types")
        void shouldHave5Types() {
            assertEquals(5, RecommendationType.values().length);
        }

        @Test
        @DisplayName("Should have values and descriptions")
        void shouldHaveValues() {
            assertEquals("collaborative", RecommendationType.COLLABORATIVE.getValue());
            assertNotNull(RecommendationType.HYBRID.getDescription());
        }

        @Test
        @DisplayName("Should parse from string")
        void shouldParseFromString() {
            assertEquals(RecommendationType.COLLABORATIVE, RecommendationType.fromString("collaborative"));
            assertEquals(RecommendationType.CONTENT_BASED, RecommendationType.fromString("CONTENT_BASED"));
        }

        @Test
        @DisplayName("Should throw on unknown type")
        void shouldThrowOnUnknown() {
            assertThrows(IllegalArgumentException.class, () -> RecommendationType.fromString("unknown"));
        }

        @Test
        @DisplayName("toString returns value")
        void toStringReturnsValue() {
            assertEquals("hybrid", RecommendationType.HYBRID.toString());
        }
    }

    @Nested
    @DisplayName("RecommendationReason Tests")
    class RecommendationReasonTest {

        @Test
        @DisplayName("Should have 5 reasons")
        void shouldHave5Reasons() {
            assertEquals(5, RecommendationReason.values().length);
        }
    }

    @Nested
    @DisplayName("RecommendationResult Tests")
    class RecommendationResultTest {

        @Test
        @DisplayName("Should build and get item count")
        void shouldBuildAndGetItemCount() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("r1").userId("u1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(List.of(
                            RecommendationItem.builder().itemId("a").title("A").score(0.8).build(),
                            RecommendationItem.builder().itemId("b").title("B").score(0.6).build()
                    ))
                    .generatedAt(Instant.now())
                    .algorithm("hybrid")
                    .confidence(0.92)
                    .build();
            assertEquals(2, result.getItemCount());
            assertEquals("r1", result.getRequestId());
            assertEquals(0.92, result.getConfidence(), 0.001);
        }

        @Test
        @DisplayName("Should handle null items")
        void shouldHandleNullItems() {
            RecommendationResult result = RecommendationResult.builder().build();
            assertEquals(0, result.getItemCount());
        }
    }

    @Nested
    @DisplayName("RecommendationPolicy Tests")
    class RecommendationPolicyTest {

        private final RecommendationPolicy policy = new RecommendationPolicy();

        private RecommendationItem makeItem(String id, double score, String category) {
            return new RecommendationItem(id, "Item " + id, category, score,
                    null, null, null, null);
        }

        @Test
        @DisplayName("Should filter by score")
        void shouldFilterByScore() {
            List<RecommendationItem> items = List.of(
                    makeItem("a", 0.9, "cat"),
                    makeItem("b", 0.2, "cat"),
                    makeItem("c", 0.5, "cat")
            );
            List<RecommendationItem> filtered = policy.filterByScore(items, 0.3);
            assertEquals(2, filtered.size());
        }

        @Test
        @DisplayName("Should apply diversity limit")
        void shouldApplyDiversity() {
            List<RecommendationItem> items = List.of(
                    makeItem("a", 0.9, "cat"),
                    makeItem("b", 0.8, "cat"),
                    makeItem("c", 0.7, "cat")
            );
            List<RecommendationItem> result = policy.applyDiversity(items);
            assertEquals(3, result.size());
            assertEquals("a", result.get(0).getItemId());
        }

        @Test
        @DisplayName("Should limit by category")
        void shouldLimitByCategory() {
            List<RecommendationItem> items = List.of(
                    makeItem("a", 0.9, "electronics"),
                    makeItem("b", 0.8, "electronics"),
                    makeItem("c", 0.7, "electronics"),
                    makeItem("d", 0.6, "books"),
                    makeItem("e", 0.5, "books")
            );
            List<RecommendationItem> result = policy.limitByCategory(items, 2);
            assertEquals(4, result.size());
        }

        @Test
        @DisplayName("Should validate recommendation type")
        void shouldValidateType() {
            assertTrue(policy.isValidRecommendationType(RecommendationType.HYBRID));
            assertFalse(policy.isValidRecommendationType(null));
        }

        @Test
        @DisplayName("Should get max recommendations")
        void shouldGetMax() {
            assertEquals(100, policy.getMaxRecommendations());
        }

        @Test
        @DisplayName("Should deduplicate results")
        void shouldDeduplicate() {
            RecommendationItem item1 = makeItem("a", 0.9, "cat");
            RecommendationItem item2 = makeItem("a", 0.8, "cat");
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("r1").userId("u1").type(RecommendationType.COLLABORATIVE)
                    .items(List.of(item1, item2)).build();
            RecommendationResult deduped = policy.deduplicateResults(result);
            assertEquals(1, deduped.getItemCount());
        }
    }
}

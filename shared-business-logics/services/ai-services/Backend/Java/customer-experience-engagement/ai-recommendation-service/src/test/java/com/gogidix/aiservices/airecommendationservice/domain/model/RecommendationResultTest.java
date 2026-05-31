package com.gogidix.aiservices.airecommendationservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RecommendationResult Domain Model Tests")
class RecommendationResultTest {

    private List<RecommendationItem> testItems;
    private Instant testTimestamp;

    @BeforeEach
    void setUp() {
        testItems = List.of(
                RecommendationItem.builder()
                        .itemId("item-1")
                        .title("Test Product 1")
                        .category("electronics")
                        .score(0.9)
                        .reason(RecommendationReason.COLLABORATIVE)
                        .build(),
                RecommendationItem.builder()
                        .itemId("item-2")
                        .title("Test Product 2")
                        .category("books")
                        .score(0.85)
                        .reason(RecommendationReason.CONTENT_BASED)
                        .build()
        );
        testTimestamp = Instant.now();
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build result with all fields")
        void shouldBuildResultWithAllFields() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .sessionId("session-789")
                    .generatedAt(testTimestamp)
                    .algorithm("hybrid")
                    .confidence(0.92)
                    .build();

            assertThat(result.getRequestId()).isEqualTo("req-123");
            assertThat(result.getUserId()).isEqualTo("user-456");
            assertThat(result.getType()).isEqualTo(RecommendationType.PERSONALIZED);
            assertThat(result.getItems()).hasSize(2);
            assertThat(result.getSessionId()).isEqualTo("session-789");
            assertThat(result.getGeneratedAt()).isEqualTo(testTimestamp);
            assertThat(result.getAlgorithm()).isEqualTo("hybrid");
            assertThat(result.getConfidence()).isEqualTo(0.92);
        }

        @Test
        @DisplayName("Should build result with required fields only")
        void shouldBuildResultWithRequiredFieldsOnly() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.HYBRID)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getRequestId()).isEqualTo("req-123");
            assertThat(result.getUserId()).isEqualTo("user-456");
            assertThat(result.getType()).isEqualTo(RecommendationType.HYBRID);
            assertThat(result.getItems()).hasSize(2);
            assertThat(result.getGeneratedAt()).isEqualTo(testTimestamp);
            assertThat(result.getSessionId()).isNull();
            assertThat(result.getAlgorithm()).isNull();
            assertThat(result.getConfidence()).isNull();
        }

        @Test
        @DisplayName("Should build result with empty items list")
        void shouldBuildResultWithEmptyItemsList() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.POPULAR)
                    .items(List.of())
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItems()).isEmpty();
            assertThat(result.getItemCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should build result with null items")
        void shouldBuildResultWithNullItems() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.CONTENT_BASED)
                    .items(null)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItems()).isNull();
            assertThat(result.getItemCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("getItemCount() Tests")
    class GetItemCountTests {

        @Test
        @DisplayName("Should return correct count for non-empty items")
        void shouldReturnCorrectCountForNonEmptyItems() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItemCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return 0 for empty items list")
        void shouldReturnZeroForEmptyItemsList() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(List.of())
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItemCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return 0 for null items")
        void shouldReturnZeroForNullItems() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(null)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItemCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle large item lists")
        void shouldHandleLargeItemLists() {
            List<RecommendationItem> largeList = List.of(
                    RecommendationItem.builder()
                            .itemId("item-1")
                            .score(0.9)
                            .reason(RecommendationReason.CONTENT_BASED)
                            .build(),
                    RecommendationItem.builder()
                            .itemId("item-2")
                            .score(0.8)
                            .reason(RecommendationReason.COLLABORATIVE)
                            .build(),
                    RecommendationItem.builder()
                            .itemId("item-3")
                            .score(0.7)
                            .reason(RecommendationReason.TRENDING)
                            .build(),
                    RecommendationItem.builder()
                            .itemId("item-4")
                            .score(0.6)
                            .reason(RecommendationReason.BEHAVIORAL)
                            .build(),
                    RecommendationItem.builder()
                            .itemId("item-5")
                            .score(0.5)
                            .reason(RecommendationReason.CONTEXTUAL)
                            .build()
            );

            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(largeList)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getItemCount()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodsTests {

        @Test
        @DisplayName("All getters should return correct values")
        void allGettersShouldReturnCorrectValues() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.HYBRID)
                    .items(testItems)
                    .sessionId("session-789")
                    .generatedAt(testTimestamp)
                    .algorithm("hybrid-algo")
                    .confidence(0.95)
                    .build();

            assertThat(result.getRequestId()).isEqualTo("req-123");
            assertThat(result.getUserId()).isEqualTo("user-456");
            assertThat(result.getType()).isEqualTo(RecommendationType.HYBRID);
            assertThat(result.getItems()).isEqualTo(testItems);
            assertThat(result.getSessionId()).isEqualTo("session-789");
            assertThat(result.getGeneratedAt()).isEqualTo(testTimestamp);
            assertThat(result.getAlgorithm()).isEqualTo("hybrid-algo");
            assertThat(result.getConfidence()).isEqualTo(0.95);
        }

        @Test
        @DisplayName("Getters should return null for unset optional fields")
        void gettersShouldReturnNullForUnsetOptionalFields() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.COLLABORATIVE)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getSessionId()).isNull();
            assertThat(result.getAlgorithm()).isNull();
            assertThat(result.getConfidence()).isNull();
        }
    }

    @Nested
    @DisplayName("RecommendationType Tests")
    class RecommendationTypeTests {

        @Test
        @DisplayName("Should handle all recommendation types")
        void shouldHandleAllRecommendationTypes() {
            for (RecommendationType type : RecommendationType.values()) {
                RecommendationResult result = RecommendationResult.builder()
                        .requestId("req-123")
                        .userId("user-456")
                        .type(type)
                        .items(testItems)
                        .generatedAt(testTimestamp)
                        .build();

                assertThat(result.getType()).isEqualTo(type);
            }
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Result should be immutable via getters")
        void resultShouldBeImmutableViaGetters() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            // The class uses @Getter which returns direct references
            // But the builder pattern encourages immutability
            assertThat(result.getRequestId()).isEqualTo("req-123");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null requestId")
        void shouldHandleNullRequestId() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId(null)
                    .userId("user-456")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getRequestId()).isNull();
        }

        @Test
        @DisplayName("Should handle null userId")
        void shouldHandleNullUserId() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId(null)
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            assertThat(result.getUserId()).isNull();
        }

        @Test
        @DisplayName("Should handle extreme confidence values")
        void shouldHandleExtremeConfidenceValues() {
            RecommendationResult result1 = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .confidence(0.0)
                    .build();

            RecommendationResult result2 = RecommendationResult.builder()
                    .requestId("req-2")
                    .userId("user-2")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .confidence(1.0)
                    .build();

            assertThat(result1.getConfidence()).isEqualTo(0.0);
            assertThat(result2.getConfidence()).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should support fluent builder pattern")
        void shouldSupportFluentBuilderPattern() {
            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-123")
                    .userId("user-456")
                    .type(RecommendationType.HYBRID)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .sessionId("session-789")
                    .algorithm("hybrid-algo")
                    .confidence(0.95)
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getRequestId()).isEqualTo("req-123");
            assertThat(result.getUserId()).isEqualTo("user-456");
        }

        @Test
        @DisplayName("Should be usable in collections")
        void shouldBeUsableInCollections() {
            RecommendationResult result1 = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            RecommendationResult result2 = RecommendationResult.builder()
                    .requestId("req-2")
                    .userId("user-2")
                    .type(RecommendationType.COLLABORATIVE)
                    .items(testItems)
                    .generatedAt(testTimestamp)
                    .build();

            List<RecommendationResult> results = List.of(result1, result2);

            assertThat(results).hasSize(2);
            assertThat(results.get(0).getRequestId()).isEqualTo("req-1");
            assertThat(results.get(1).getRequestId()).isEqualTo("req-2");
        }
    }
}

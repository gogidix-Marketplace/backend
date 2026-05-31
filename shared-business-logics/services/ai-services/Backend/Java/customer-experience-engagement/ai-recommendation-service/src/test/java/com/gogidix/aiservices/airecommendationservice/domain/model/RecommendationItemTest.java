package com.gogidix.aiservices.airecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RecommendationItem Domain Model Tests")
class RecommendationItemTest {

    private static final String ITEM_ID = "item-123";

    @Nested
    @DisplayName("RecommendationItem Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create item with valid parameters")
        void shouldCreateWithValidParameters() {
            RecommendationItem item = RecommendationItem.builder()
                    .itemId(ITEM_ID)
                    .title("Test Product")
                    .category("electronics")
                    .score(0.85)
                    .reason(RecommendationReason.COLLABORATIVE)
                    .build();

            assertThat(item).isNotNull();
            assertThat(item.getItemId()).isEqualTo(ITEM_ID);
            assertThat(item.getScore()).isEqualTo(0.85);
        }

        @Test
        @DisplayName("Should reject null item ID")
        void shouldRejectNullItemId() {
            assertThatThrownBy(() -> RecommendationItem.builder()
                    .itemId(null)
                    .title("Test")
                    .score(0.5)
                    .reason(RecommendationReason.CONTENT_BASED)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject score outside range")
        void shouldRejectInvalidScore() {
            assertThatThrownBy(() -> RecommendationItem.builder()
                    .itemId(ITEM_ID)
                    .score(1.5)
                    .reason(RecommendationReason.CONTENT_BASED)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Score Threshold Tests")
    class ThresholdTests {

        @Test
        @DisplayName("Should check score threshold")
        void shouldCheckThreshold() {
            RecommendationItem item = RecommendationItem.builder()
                    .itemId(ITEM_ID)
                    .score(0.75)
                    .reason(RecommendationReason.CONTENT_BASED)
                    .build();

            assertThat(item.meetsThreshold(0.7)).isTrue();
            assertThat(item.meetsThreshold(0.8)).isFalse();
        }
    }

    @Nested
    @DisplayName("RecommendationType Tests")
    class TypeTests {

        @ParameterizedTest
        @EnumSource(RecommendationType.class)
        @DisplayName("Should accept all recommendation types")
        void shouldAcceptAllTypes(RecommendationType type) {
            assertThat(type).isNotNull();
            assertThat(RecommendationType.fromString(type.toString())).isEqualTo(type);
        }
    }

    @Nested
    @DisplayName("RecommendationResult Tests")
    class ResultTests {

        @Test
        @DisplayName("Should create recommendation result")
        void shouldCreateResult() {
            List<RecommendationItem> items = List.of(
                    RecommendationItem.builder()
                            .itemId("item-1")
                            .score(0.9)
                            .reason(RecommendationReason.COLLABORATIVE)
                            .build()
            );

            RecommendationResult result = RecommendationResult.builder()
                    .requestId("req-1")
                    .userId("user-1")
                    .type(RecommendationType.PERSONALIZED)
                    .items(items)
                    .generatedAt(Instant.now())
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getItemCount()).isEqualTo(1);
        }
    }
}

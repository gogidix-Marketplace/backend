package com.gogidix.aiservices.aiproductrecommendationservice.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain Event Tests")
class DomainEventTest {

    @Nested
    @DisplayName("RecommendationCreatedEvent Tests")
    class RecommendationCreatedEventTests {
        @Test
        void shouldCreateEvent() {
            Instant now = Instant.now();
            RecommendationCreatedEvent event = new RecommendationCreatedEvent("agg-1", "tenant-1", "Premium", "BEHAVIORAL", now);
            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getAggregateId()).isEqualTo("agg-1");
            assertThat(event.getTenantId()).isEqualTo("tenant-1");
            assertThat(event.getRecommendationName()).isEqualTo("Premium");
            assertThat(event.getRecommendationType()).isEqualTo("BEHAVIORAL");
            assertThat(event.getOccurredAt()).isEqualTo(now);
        }

        @Test
        void shouldRejectNullAggregateId() {
            assertThatThrownBy(() -> new RecommendationCreatedEvent(null, "t", "n", "T", Instant.now()))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldImplementEquals() {
            RecommendationCreatedEvent e1 = new RecommendationCreatedEvent("a", "t", "n", "T", Instant.now());
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
            assertThat(e1).isNotEqualTo("string");
        }

        @Test
        void shouldHaveToString() {
            RecommendationCreatedEvent e1 = new RecommendationCreatedEvent("a", "t", "n", "T", Instant.now());
            assertThat(e1.toString()).contains("RecommendationCreatedEvent");
        }
    }

    @Nested
    @DisplayName("RecommendationUpdatedEvent Tests")
    class RecommendationUpdatedEventTests {
        @Test
        void shouldCreateEvent() {
            Instant now = Instant.now();
            RecommendationUpdatedEvent event = new RecommendationUpdatedEvent("agg-1", "tenant-1", "Old", "New", "RENAME", now);
            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getOldName()).isEqualTo("Old");
            assertThat(event.getNewName()).isEqualTo("New");
            assertThat(event.getChangeType()).isEqualTo("RENAME");
        }

        @Test
        void shouldImplementEquals() {
            RecommendationUpdatedEvent e1 = new RecommendationUpdatedEvent("a", "t", "o", "n", "T", Instant.now());
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
        }
    }

    @Nested
    @DisplayName("RecommendationDeletedEvent Tests")
    class RecommendationDeletedEventTests {
        @Test
        void shouldCreateEvent() {
            Instant now = Instant.now();
            RecommendationDeletedEvent event = new RecommendationDeletedEvent("agg-1", "tenant-1", "Rec1", "HARD_DELETE", 50L, now);
            assertThat(event.getRecommendationName()).isEqualTo("Rec1");
            assertThat(event.getDeletionType()).isEqualTo("HARD_DELETE");
            assertThat(event.getProductCount()).isEqualTo(50L);
        }

        @Test
        void shouldAllowNullProductCount() {
            RecommendationDeletedEvent event = new RecommendationDeletedEvent("a", "t", "n", "SOFT", null, Instant.now());
            assertThat(event.getProductCount()).isNull();
        }
    }

    @Nested
    @DisplayName("ProductsAddedToRecommendationEvent Tests")
    class ProductsAddedToRecommendationEventTests {
        @Test
        void shouldCreateEvent() {
            Instant now = Instant.now();
            ProductsAddedToRecommendationEvent event = new ProductsAddedToRecommendationEvent("agg-1", "tenant-1", "Rec1", 25, 100L, now);
            assertThat(event.getProductsAdded()).isEqualTo(25);
            assertThat(event.getTotalProductCount()).isEqualTo(100L);
        }

        @Test
        void shouldRejectNullProductsAdded() {
            assertThatThrownBy(() -> new ProductsAddedToRecommendationEvent("a", "t", "n", null, 1L, Instant.now()))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}

package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain Event Tests")
class DomainEventTest {

    @Nested
    @DisplayName("BasketCreatedEvent Tests")
    class BasketCreatedEventTests {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            BasketCreatedEvent event = new BasketCreatedEvent("agg-1", "tenant-1", "Premium", "BEHAVIORAL", now);

            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getAggregateId()).isEqualTo("agg-1");
            assertThat(event.getTenantId()).isEqualTo("tenant-1");
            assertThat(event.getBasketName()).isEqualTo("Premium");
            assertThat(event.getBasketType()).isEqualTo("BEHAVIORAL");
            assertThat(event.getOccurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should reject null aggregateId")
        void shouldRejectNullAggregateId() {
            assertThatThrownBy(() -> new BasketCreatedEvent(null, "t", "n", "T", Instant.now()))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("aggregateId");
        }

        @Test
        @DisplayName("Should reject null tenantId")
        void shouldRejectNullTenantId() {
            assertThatThrownBy(() -> new BasketCreatedEvent("a", null, "n", "T", Instant.now()))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should implement equals by eventId")
        void shouldImplementEquals() {
            Instant now = Instant.now();
            BasketCreatedEvent e1 = new BasketCreatedEvent("a", "t", "n", "T", now);
            BasketCreatedEvent e2 = new BasketCreatedEvent("a", "t", "n", "T", now);
            assertThat(e1).isNotEqualTo(e2);
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
            assertThat(e1).isNotEqualTo("string");
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            BasketCreatedEvent e1 = new BasketCreatedEvent("a", "t", "n", "T", Instant.now());
            assertThat(e1.hashCode()).isEqualTo(e1.hashCode());
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveToString() {
            BasketCreatedEvent e1 = new BasketCreatedEvent("a", "t", "n", "T", Instant.now());
            assertThat(e1.toString()).contains("BasketCreatedEvent");
            assertThat(e1.toString()).contains("a");
        }
    }

    @Nested
    @DisplayName("BasketUpdatedEvent Tests")
    class BasketUpdatedEventTests {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            BasketUpdatedEvent event = new BasketUpdatedEvent("agg-1", "tenant-1", "Old", "New", "RENAME", now);

            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getAggregateId()).isEqualTo("agg-1");
            assertThat(event.getTenantId()).isEqualTo("tenant-1");
            assertThat(event.getOldName()).isEqualTo("Old");
            assertThat(event.getNewName()).isEqualTo("New");
            assertThat(event.getChangeType()).isEqualTo("RENAME");
            assertThat(event.getOccurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should reject null newName")
        void shouldRejectNullNewName() {
            assertThatThrownBy(() -> new BasketUpdatedEvent("a", "t", "old", null, "T", Instant.now()))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should implement equals by eventId")
        void shouldImplementEquals() {
            Instant now = Instant.now();
            BasketUpdatedEvent e1 = new BasketUpdatedEvent("a", "t", "o", "n", "T", now);
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
            assertThat(e1).isNotEqualTo("string");
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveToString() {
            BasketUpdatedEvent e1 = new BasketUpdatedEvent("a", "t", "o", "n", "T", Instant.now());
            assertThat(e1.toString()).contains("BasketUpdatedEvent");
        }
    }

    @Nested
    @DisplayName("BasketDeletedEvent Tests")
    class BasketDeletedEventTests {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            BasketDeletedEvent event = new BasketDeletedEvent("agg-1", "tenant-1", "Basket1", "HARD_DELETE", 50L, now);

            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getAggregateId()).isEqualTo("agg-1");
            assertThat(event.getTenantId()).isEqualTo("tenant-1");
            assertThat(event.getBasketName()).isEqualTo("Basket1");
            assertThat(event.getDeletionType()).isEqualTo("HARD_DELETE");
            assertThat(event.getCustomerCount()).isEqualTo(50L);
            assertThat(event.getOccurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should allow null customerCount")
        void shouldAllowNullCustomerCount() {
            BasketDeletedEvent event = new BasketDeletedEvent("a", "t", "n", "SOFT", null, Instant.now());
            assertThat(event.getCustomerCount()).isNull();
        }

        @Test
        @DisplayName("Should implement equals by eventId")
        void shouldImplementEquals() {
            BasketDeletedEvent e1 = new BasketDeletedEvent("a", "t", "n", "T", 1L, Instant.now());
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveToString() {
            BasketDeletedEvent e1 = new BasketDeletedEvent("a", "t", "n", "T", 1L, Instant.now());
            assertThat(e1.toString()).contains("BasketDeletedEvent");
        }
    }

    @Nested
    @DisplayName("CustomersAddedToBasketEvent Tests")
    class CustomersAddedToBasketEventTests {

        @Test
        @DisplayName("Should create event with all fields")
        void shouldCreateEvent() {
            Instant now = Instant.now();
            CustomersAddedToBasketEvent event = new CustomersAddedToBasketEvent("agg-1", "tenant-1", "Basket1", 25, 100L, now);

            assertThat(event.getEventId()).isNotNull();
            assertThat(event.getAggregateId()).isEqualTo("agg-1");
            assertThat(event.getTenantId()).isEqualTo("tenant-1");
            assertThat(event.getBasketName()).isEqualTo("Basket1");
            assertThat(event.getCustomersAdded()).isEqualTo(25);
            assertThat(event.getTotalCustomerCount()).isEqualTo(100L);
            assertThat(event.getOccurredAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should reject null customersAdded")
        void shouldRejectNullCustomersAdded() {
            assertThatThrownBy(() -> new CustomersAddedToBasketEvent("a", "t", "n", null, 1L, Instant.now()))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should implement equals by eventId")
        void shouldImplementEquals() {
            CustomersAddedToBasketEvent e1 = new CustomersAddedToBasketEvent("a", "t", "n", 1, 2L, Instant.now());
            assertThat(e1).isEqualTo(e1);
            assertThat(e1).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveToString() {
            CustomersAddedToBasketEvent e1 = new CustomersAddedToBasketEvent("a", "t", "n", 1, 2L, Instant.now());
            assertThat(e1.toString()).contains("CustomersAddedToBasketEvent");
        }
    }
}

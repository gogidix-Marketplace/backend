package com.gogidix.aiservices.aisalesforecastingservice.domain.event;

import org.junit.jupiter.api.*;
import java.time.Instant;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class DomainEventTest {

    @Nested
    class ForecastCreatedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            Instant now = Instant.now();
            var evt = new ForecastCreatedEvent("agg1", "t1", "name1", "type1", now);
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getAggregateId()).isEqualTo("agg1");
            assertThat(evt.getTenantId()).isEqualTo("t1");
            assertThat(evt.getOccurredAt()).isEqualTo(now);
            assertThat(evt.getForecastName()).isEqualTo("name1");
            assertThat(evt.getForecastType()).isEqualTo("type1");
        }

        @Test
        void shouldThrowOnNullArgs() {
            assertThatThrownBy(() -> new ForecastCreatedEvent(null, "t", "n", "s", Instant.now()))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new ForecastCreatedEvent("a", null, "n", "s", Instant.now()))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new ForecastCreatedEvent("a", "t", null, "s", Instant.now()))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new ForecastCreatedEvent("a", "t", "n", null, Instant.now()))
                .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> new ForecastCreatedEvent("a", "t", "n", "s", null))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldImplementEquality() {
            var evt = new ForecastCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.equals(evt)).isTrue();
            assertThat(evt.equals(null)).isFalse();
            assertThat(evt.equals("string")).isFalse();
            var other = new ForecastCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.equals(other)).isFalse();
            assertThat(evt.hashCode()).isNotEqualTo(other.hashCode());
        }

        @Test
        void shouldHaveToString() {
            var evt = new ForecastCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.toString()).contains("ForecastCreatedEvent");
        }
    }

    @Nested
    class ForecastDeletedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            Instant now = Instant.now();
            var evt = new ForecastDeletedEvent("agg1", "t1", "name1", "hard", 5L, now);
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getAggregateId()).isEqualTo("agg1");
            assertThat(evt.getTenantId()).isEqualTo("t1");
            assertThat(evt.getOccurredAt()).isEqualTo(now);
            assertThat(evt.getForecastName()).isEqualTo("name1");
            assertThat(evt.getDeletionType()).isEqualTo("hard");
            assertThat(evt.getForecastModelCount()).isEqualTo(5L);
        }

        @Test
        void shouldThrowOnNullArgs() {
            assertThatThrownBy(() -> new ForecastDeletedEvent(null, "t", "n", "d", 1L, Instant.now()))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldImplementEquality() {
            var evt = new ForecastDeletedEvent("a", "t", "n", "d", 1L, Instant.now());
            assertThat(evt.equals(evt)).isTrue();
            assertThat(evt.equals(null)).isFalse();
            var other = new ForecastDeletedEvent("a", "t", "n", "d", 1L, Instant.now());
            assertThat(evt.equals(other)).isFalse();
        }

        @Test
        void shouldHaveToString() {
            var evt = new ForecastDeletedEvent("a", "t", "n", "d", 1L, Instant.now());
            assertThat(evt.toString()).contains("ForecastDeletedEvent");
        }
    }

    @Nested
    class ForecastUpdatedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            Instant now = Instant.now();
            var evt = new ForecastUpdatedEvent("agg1", "t1", "old", "new", "rename", now);
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getAggregateId()).isEqualTo("agg1");
            assertThat(evt.getOldName()).isEqualTo("old");
            assertThat(evt.getNewName()).isEqualTo("new");
            assertThat(evt.getChangeType()).isEqualTo("rename");
        }

        @Test
        void shouldThrowOnNullArgs() {
            assertThatThrownBy(() -> new ForecastUpdatedEvent(null, "t", "o", "n", "c", Instant.now()))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldImplementEquality() {
            var evt = new ForecastUpdatedEvent("a", "t", "o", "n", "c", Instant.now());
            assertThat(evt.equals(evt)).isTrue();
            assertThat(evt.equals(null)).isFalse();
            var other = new ForecastUpdatedEvent("a", "t", "o", "n", "c", Instant.now());
            assertThat(evt.equals(other)).isFalse();
        }

        @Test
        void shouldHaveToString() {
            var evt = new ForecastUpdatedEvent("a", "t", "o", "n", "c", Instant.now());
            assertThat(evt.toString()).contains("ForecastUpdatedEvent");
        }
    }

    @Nested
    class ForecastModelsAddedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            Instant now = Instant.now();
            var evt = new ForecastModelsAddedEvent("agg1", "t1", "name1", 5, 10L, now);
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getAggregateId()).isEqualTo("agg1");
            assertThat(evt.getTenantId()).isEqualTo("t1");
            assertThat(evt.getForecastName()).isEqualTo("name1");
            assertThat(evt.getForecastModelsAdded()).isEqualTo(5);
            assertThat(evt.getTotalForecastModelCount()).isEqualTo(10L);
        }

        @Test
        void shouldThrowOnNullArgs() {
            assertThatThrownBy(() -> new ForecastModelsAddedEvent(null, "t", "n", 1, 2L, Instant.now()))
                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void shouldImplementEquality() {
            var evt = new ForecastModelsAddedEvent("a", "t", "n", 1, 2L, Instant.now());
            assertThat(evt.equals(evt)).isTrue();
            assertThat(evt.equals(null)).isFalse();
            var other = new ForecastModelsAddedEvent("a", "t", "n", 1, 2L, Instant.now());
            assertThat(evt.equals(other)).isFalse();
        }

        @Test
        void shouldHaveToString() {
            var evt = new ForecastModelsAddedEvent("a", "t", "n", 1, 2L, Instant.now());
            assertThat(evt.toString()).contains("ForecastModelsAddedEvent");
        }
    }
}

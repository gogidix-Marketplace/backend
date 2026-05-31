package com.gogidix.aiservices.aiuserprofilingservice.domain.event;

import org.junit.jupiter.api.*;
import java.time.Instant;
import static org.assertj.core.api.Assertions.*;

class DomainEventTest {

    @Nested
    class ProfileCreatedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            var evt = new ProfileCreatedEvent("agg1", "t1", "name1", "type1", Instant.now());
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getAggregateId()).isEqualTo("agg1");
            assertThat(evt.getTenantId()).isEqualTo("t1");
            assertThat(evt.getProfileName()).isEqualTo("name1");
            assertThat(evt.getProfileType()).isEqualTo("type1");
        }
        @Test
        void shouldThrowOnNull() {
            assertThatThrownBy(() -> new ProfileCreatedEvent(null, "t", "n", "s", Instant.now()))
                .isInstanceOf(NullPointerException.class);
        }
        @Test
        void equalityTest() {
            var evt = new ProfileCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.equals(evt)).isTrue();
            assertThat(evt.equals(null)).isFalse();
            var other = new ProfileCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.equals(other)).isFalse();
        }
        @Test
        void toStringTest() {
            var evt = new ProfileCreatedEvent("a", "t", "n", "s", Instant.now());
            assertThat(evt.toString()).contains("ProfileCreatedEvent");
        }
    }

    @Nested
    class ProfileDeletedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            var evt = new ProfileDeletedEvent("agg1", "t1", "name1", "hard", 5L, Instant.now());
            assertThat(evt.getEventId()).isNotNull();
            assertThat(evt.getProfileName()).isEqualTo("name1");
            assertThat(evt.getDeletionType()).isEqualTo("hard");
            assertThat(evt.getUserCount()).isEqualTo(5L);
        }
        @Test
        void equalityTest() {
            var evt = new ProfileDeletedEvent("a", "t", "n", "d", 1L, Instant.now());
            assertThat(evt.equals(null)).isFalse();
        }
    }

    @Nested
    class ProfileUpdatedEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            var evt = new ProfileUpdatedEvent("agg1", "t1", "old", "new", "rename", Instant.now());
            assertThat(evt.getOldName()).isEqualTo("old");
            assertThat(evt.getNewName()).isEqualTo("new");
            assertThat(evt.getChangeType()).isEqualTo("rename");
        }
    }

    @Nested
    class UsersAddedToProfileEventTests {
        @Test
        void shouldCreateAndGetProperties() {
            var evt = new UsersAddedToProfileEvent("agg1", "t1", "name1", 5, 10L, Instant.now());
            assertThat(evt.getProfileName()).isEqualTo("name1");
            assertThat(evt.getUsersAdded()).isEqualTo(5);
            assertThat(evt.getTotalUserCount()).isEqualTo(10L);
        }
    }
}

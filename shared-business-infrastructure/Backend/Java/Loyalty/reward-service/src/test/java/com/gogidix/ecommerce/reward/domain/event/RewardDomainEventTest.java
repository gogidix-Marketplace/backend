package com.gogidix.ecommerce.reward.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Reward Domain Event Tests")
class RewardDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        RewardCreatedEvent event = new RewardCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Reward_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        RewardUpdatedEvent event = new RewardUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Reward_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        RewardDeletedEvent event = new RewardDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Reward_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        RewardCreatedEvent event = new RewardCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(RewardDomainEvent.class);
    }
}

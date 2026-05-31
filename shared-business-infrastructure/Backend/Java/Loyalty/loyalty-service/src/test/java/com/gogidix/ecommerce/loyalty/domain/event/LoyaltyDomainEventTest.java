package com.gogidix.ecommerce.loyalty.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Loyalty Domain Event Tests")
class LoyaltyDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        LoyaltyCreatedEvent event = new LoyaltyCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Loyalty_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        LoyaltyUpdatedEvent event = new LoyaltyUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Loyalty_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        LoyaltyDeletedEvent event = new LoyaltyDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Loyalty_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        LoyaltyCreatedEvent event = new LoyaltyCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(LoyaltyDomainEvent.class);
    }
}

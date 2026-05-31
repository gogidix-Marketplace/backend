package com.gogidix.ecommerce.airfreight.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AirFreight Domain Event Tests")
class AirFreightDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        AirFreightCreatedEvent event = new AirFreightCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("AirFreight_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        AirFreightUpdatedEvent event = new AirFreightUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("AirFreight_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        AirFreightDeletedEvent event = new AirFreightDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("AirFreight_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        AirFreightCreatedEvent event = new AirFreightCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(AirFreightDomainEvent.class);
    }
}

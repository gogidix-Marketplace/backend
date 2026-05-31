package com.gogidix.ecommerce.communication.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Communication Domain Event Tests")
class CommunicationDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        CommunicationCreatedEvent event = new CommunicationCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Communication_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        CommunicationUpdatedEvent event = new CommunicationUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Communication_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        CommunicationDeletedEvent event = new CommunicationDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Communication_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        CommunicationCreatedEvent event = new CommunicationCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(CommunicationDomainEvent.class);
    }
}

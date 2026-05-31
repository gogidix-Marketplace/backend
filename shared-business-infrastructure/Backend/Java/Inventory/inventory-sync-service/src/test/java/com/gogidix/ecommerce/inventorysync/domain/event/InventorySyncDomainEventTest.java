package com.gogidix.ecommerce.inventorysync.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("InventorySync Domain Event Tests")
class InventorySyncDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        InventorySyncCreatedEvent event = new InventorySyncCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("InventorySync_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        InventorySyncUpdatedEvent event = new InventorySyncUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("InventorySync_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        InventorySyncDeletedEvent event = new InventorySyncDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("InventorySync_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        InventorySyncCreatedEvent event = new InventorySyncCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(InventorySyncDomainEvent.class);
    }
}

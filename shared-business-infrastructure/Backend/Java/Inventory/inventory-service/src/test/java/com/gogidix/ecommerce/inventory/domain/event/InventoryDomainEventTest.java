package com.gogidix.ecommerce.inventory.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Inventory Domain Event Tests")
class InventoryDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        InventoryCreatedEvent event = new InventoryCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Inventory_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        InventoryUpdatedEvent event = new InventoryUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Inventory_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        InventoryDeletedEvent event = new InventoryDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Inventory_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        InventoryCreatedEvent event = new InventoryCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(InventoryDomainEvent.class);
    }
}

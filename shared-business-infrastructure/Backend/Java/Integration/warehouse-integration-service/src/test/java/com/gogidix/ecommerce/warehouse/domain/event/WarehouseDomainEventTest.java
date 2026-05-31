package com.gogidix.ecommerce.warehouse.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Warehouse Domain Event Tests")
class WarehouseDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Warehouse_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        WarehouseUpdatedEvent event = new WarehouseUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Warehouse_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        WarehouseDeletedEvent event = new WarehouseDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Warehouse_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        WarehouseCreatedEvent event = new WarehouseCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(WarehouseDomainEvent.class);
    }
}

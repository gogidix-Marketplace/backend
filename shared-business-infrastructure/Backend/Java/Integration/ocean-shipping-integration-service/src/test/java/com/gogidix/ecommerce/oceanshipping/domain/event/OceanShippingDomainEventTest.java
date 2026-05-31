package com.gogidix.ecommerce.oceanshipping.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OceanShipping Domain Event Tests")
class OceanShippingDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        OceanShippingCreatedEvent event = new OceanShippingCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("OceanShipping_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        OceanShippingUpdatedEvent event = new OceanShippingUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("OceanShipping_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        OceanShippingDeletedEvent event = new OceanShippingDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("OceanShipping_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        OceanShippingCreatedEvent event = new OceanShippingCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(OceanShippingDomainEvent.class);
    }
}

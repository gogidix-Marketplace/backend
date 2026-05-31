package com.gogidix.ecommerce.discount.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Discount Domain Event Tests")
class DiscountDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        DiscountCreatedEvent event = new DiscountCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Discount_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        DiscountUpdatedEvent event = new DiscountUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Discount_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        DiscountDeletedEvent event = new DiscountDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Discount_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        DiscountCreatedEvent event = new DiscountCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(DiscountDomainEvent.class);
    }
}

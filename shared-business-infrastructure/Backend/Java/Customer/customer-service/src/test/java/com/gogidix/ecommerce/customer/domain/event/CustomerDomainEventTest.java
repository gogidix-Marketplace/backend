package com.gogidix.ecommerce.customer.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Customer Domain Event Tests")
class CustomerDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        CustomerCreatedEvent event = new CustomerCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Customer_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        CustomerUpdatedEvent event = new CustomerUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Customer_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        CustomerDeletedEvent event = new CustomerDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Customer_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        CustomerCreatedEvent event = new CustomerCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(CustomerDomainEvent.class);
    }
}

package com.gogidix.ecommerce.paymentmethod.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentMethod Domain Event Tests")
class PaymentMethodDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        PaymentMethodCreatedEvent event = new PaymentMethodCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("PaymentMethod_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        PaymentMethodUpdatedEvent event = new PaymentMethodUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PaymentMethod_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        PaymentMethodDeletedEvent event = new PaymentMethodDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PaymentMethod_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        PaymentMethodCreatedEvent event = new PaymentMethodCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(PaymentMethodDomainEvent.class);
    }
}

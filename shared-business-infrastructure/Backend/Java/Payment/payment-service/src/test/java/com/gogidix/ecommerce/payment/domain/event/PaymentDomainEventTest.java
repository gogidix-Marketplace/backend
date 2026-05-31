package com.gogidix.ecommerce.payment.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Payment Domain Event Tests")
class PaymentDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        PaymentCreatedEvent event = new PaymentCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Payment_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        PaymentUpdatedEvent event = new PaymentUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Payment_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        PaymentDeletedEvent event = new PaymentDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Payment_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        PaymentCreatedEvent event = new PaymentCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(PaymentDomainEvent.class);
    }
}

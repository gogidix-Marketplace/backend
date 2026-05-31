package com.gogidix.ecommerce.paymentgateway.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PaymentGateway Domain Event Tests")
class PaymentGatewayDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        PaymentGatewayCreatedEvent event = new PaymentGatewayCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("PaymentGateway_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        PaymentGatewayUpdatedEvent event = new PaymentGatewayUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PaymentGateway_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        PaymentGatewayDeletedEvent event = new PaymentGatewayDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PaymentGateway_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        PaymentGatewayCreatedEvent event = new PaymentGatewayCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(PaymentGatewayDomainEvent.class);
    }
}
